import { fetchAfterResponseCodeCheck } from "../middleware/checkResponseCode.js";
import { properties } from "../properties.js";

async function getOrders(){
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/orders");
    return await response.json();
}

async function getOrderDetails(id){
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/orders/" + id);
    return await response.json();
}

async function setUpOrders(){
    document.getElementById("actions").replaceChildren();
    document.getElementById("order").replaceChildren();
    document.getElementById("orders").replaceChildren();

    var ordersElement = document.getElementById("orders");

    const orders = await getOrders();
    for(let order of orders){
        var orderElement = document.createElement("li");
        orderElement.innerHTML = "Order id: " + order.id + ". Order made at: " + order.createdAt;
        orderElement.onclick = () => {setUpOrderDetails(order.id);};

        ordersElement.appendChild(orderElement);
    }
}

async function setUpOrderDetails(id){
    document.getElementById("actions").replaceChildren();
    document.getElementById("order").replaceChildren();
    document.getElementById("orders").replaceChildren();

    var ordersButton = document.createElement("button");
    ordersButton.innerText = "See All Orders";
    ordersButton.onclick = () => {setUpOrders();};
    document.getElementById("actions").appendChild(ordersButton);

    var orderElement = document.getElementById("order");

    const order = await getOrderDetails(id);
    
    const idElement = document.createElement("p");
    idElement.innerHTML = "Order id: " + order.id;
    orderElement.appendChild(idElement);

    var totalPrice = 0.0;
    for(var index in order.vendors)
    {
        var segmentElement = document.createElement("div");
        
        var vendorElement = document.createElement("h3");
        vendorElement.innerText = order.vendors[index];
        segmentElement.appendChild(vendorElement);

        var itemsElement = document.createElement("ol");
        for(var innerIndex in order.itemLists[index])
        {
            var itemElement = document.createElement("li");
            itemElement.innerText = order.itemLists[index][innerIndex] + " : " + order.qtyLists[index][innerIndex];
            itemsElement.appendChild(itemElement);
        }
        segmentElement.appendChild(itemsElement);

        var priceElement = document.createElement("p");
        priceElement.innerText = "Price: " + order.prices[index];
        segmentElement.appendChild(priceElement);

        totalPrice = totalPrice + Number(order.prices[index]);

        var statusElement = document.createElement("p");
        if(order.statuses[index]==="ASKING")
            statusElement.innerText = "Order sent to Restaurant. Waiting for acceptance.";
        else if(order.statuses[index]==="INPROGRESS")
            statusElement.innerText = "Order accepted by Restaurant. Food is being prepared.";
        else if(order.statuses[index]==="REJECTED")
            statusElement.innerText = "Order rejected by Restaurant.";
        else if(order.statuses[index]==="TRANSPORTING")
            statusElement.innerText = "Food prepared. Out for delivery.";
        else if(order.statuses[index]==="DELIVERED")
            statusElement.innerText = "Order delivered. Enjoy!";
        else 
            statusElement.innerText = "Error fetching status.";
        segmentElement.appendChild(statusElement);

        segmentElement.appendChild(document.createElement("br"));

        orderElement.appendChild(segmentElement);
    }

    const totalPriceElement = document.createElement("p");
    totalPriceElement.innerText = "Total Price: " + totalPrice;
    orderElement.appendChild(totalPriceElement);

}

document.addEventListener('DOMContentLoaded', () => {

    setUpOrders();
});