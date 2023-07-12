
import { fetchAfterResponseCodeCheck } from "../middleware/checkResponseCode.js";
import { ItemChange } from "../model/ItemChange.js";
import { properties } from "../properties.js";

async function getItems() {
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure2/items");
    
        const responseBody = (await response.json());
        
        if(responseBody.length==0)
            return;
        for(let item of responseBody)
        {
            let itemElement = document.createElement("p");
            itemElement.setAttribute("name", "menu-item");
            itemElement.setAttribute("id", item.name);
            itemElement.setAttribute("data-price", item.price);
            itemElement.innerHTML = item.name + " = " + item.price;

            let nameElement = document.createElement("input");
            nameElement.setAttribute("id", "new-name");
            nameElement.setAttribute("placeholder", "Enter new name");

            let priceElement = document.createElement("input");
            priceElement.setAttribute("type", "number");
            priceElement.setAttribute("id", "new-price");
            priceElement.setAttribute("min", "0");
            priceElement.setAttribute("value", "0");

            let priceLabel = document.createElement("label");
            priceLabel.setAttribute("for", "new-price");
            priceLabel.innerText = " Enter new price ";

            let deleteLabel = document.createElement("label");
            deleteLabel.setAttribute("for", "delete-item");
            deleteLabel.innerText = "Tick to delete item";

            let deleteElement = document.createElement("input");
            deleteElement.setAttribute("id", "delete-item");
            deleteElement.setAttribute("type", "checkbox");

            itemElement.append(nameElement);
            itemElement.append(priceLabel);
            itemElement.append(priceElement);
            itemElement.append(deleteLabel);
            itemElement.append(deleteElement);

            document.getElementById("menu-list").append(itemElement);
        }
    
    return null;
}

async function collectItemChanges() {

    let items = document.getElementsByName("menu-item");

    let itemChanges = new Array();
    for(let item of items)
    {
        const name = item.id;
        const price = item.getAttribute("data-price");
        let newName = item.querySelector("#new-name").value;
        let newPrice = item.querySelector("#new-price").value;
        const deleteItem = item.querySelector("#delete-item").checked;
        if(newPrice==0)
            newPrice = price;
        if(newName=="")
            newName = name;
        if(deleteItem)
            newName = "";
        if(price!=newPrice || name!=newName)
        {
            let itemChange = new ItemChange(name, newName, newPrice);
            itemChanges.push(itemChange);
        }
    }

    let newItems = document.getElementsByName("new-entry");

    for(let item of newItems)
    {
        const newName = item.querySelector("#new-name").value;
        const newPrice = item.querySelector("#new-price").value;
        if(newPrice!=0 || newName!="")
        {
            let itemChange = new ItemChange("", newName, newPrice);
            itemChanges.push(itemChange);
        }
    }
    return itemChanges;
}

async function setItems(itemChanges) {
   
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure2/items", {
        method : 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
            },
        body : JSON.stringify(itemChanges)
    });

    window.location.reload();
}

window.addEventListener('DOMContentLoaded', async () => {
    await getItems();

    document.getElementById("new-item-entry").addEventListener('click', async () => {
        let newEntryElement = document.createElement("div");
        newEntryElement.setAttribute("name", "new-entry");

        let nameElement = document.createElement("input");
        nameElement.setAttribute("id", "new-name");
        nameElement.setAttribute("placeholder", "Enter new name");

        let priceElement = document.createElement("input");
        priceElement.setAttribute("type", "number");
        priceElement.setAttribute("id", "new-price");
        priceElement.setAttribute("value", "0");

        let priceLabel = document.createElement("label");
        priceLabel.setAttribute("for", "new-price");
        priceLabel.innerText = " Enter new price ";

        newEntryElement.appendChild(nameElement);
        newEntryElement.append(priceLabel);
        newEntryElement.appendChild(priceElement);

        document.getElementById("new-items").append(newEntryElement);
    });

    document.getElementById("menu-changes").addEventListener('click', async () => {
        let itemChanges = await collectItemChanges();
        if(itemChanges.length!=0)
            setItems(itemChanges);
    });
});
