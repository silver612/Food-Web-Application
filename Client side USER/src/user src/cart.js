import { fetchAfterResponseCodeCheck } from "../middleware/checkResponseCode.js";
import { CartItem } from "../model/CartItem.js";
import { properties } from "../properties.js";
import { toJson, toMap } from "../utility/mapToJson.js";


async function getCart(){
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/cart");

    const cart = await response.json();
    return cart;
}

async function setUpCart(){
    let cart = await getCart();
    const cartElement = document.getElementById("cart-items");
    cartElement.replaceChildren();
    
    if(cart.length==0)
    {
        cartElement.innerText = "No Item present in cart";
        document.getElementById("billing-div").innerText = "";
        document.getElementById("order-button").disabled = true;
    }
    else
    {
        document.getElementById("order-button").disabled = false;

        let groupedCart = new Map();
        let itemPrice = new Map();
        for(let item of cart)
        {
            let vendorItems = groupedCart.get(item.vendor);
            if(vendorItems==undefined)
            {
                groupedCart.set(item.vendor, new Map());
                vendorItems = groupedCart.get(item.vendor);
            }
            vendorItems.set(item.itemName, item.amount); 

            let vendorItemsPrice = itemPrice.get(item.vendor);
            if(vendorItemsPrice==undefined)
            {
                itemPrice.set(item.vendor, new Map());
                vendorItemsPrice = itemPrice.get(item.vendor);
            }
            vendorItemsPrice.set(item.itemName, item.price);
        }

        window.localStorage.setItem("cart", toJson(groupedCart));
        
        let totalPrice = 0.0;
        for(let [vendor, vendorItems] of groupedCart)
        {
            let vendorElement = document.createElement("div");
            vendorElement.innerHTML = "<h3>Items from " + vendor + " : </h3>";
            for(let [itemName, itemAmount] of vendorItems)
            {
                let itemElement = document.createElement("div");
                itemElement.innerText = itemName + " : " + itemAmount;
                
                let incButton = document.createElement("button");
                incButton.innerText = "+";
                incButton.onclick = () => {
                    let storedCart = toMap(window.localStorage.getItem("cart"));
                    let vendorItems = storedCart.get(vendor);
                    vendorItems.set(itemName, vendorItems.get(itemName) + 1);
                    window.localStorage.setItem("cart", toJson(storedCart));
                    saveCart(storedCart);
                }
                itemElement.appendChild(incButton);

                let decButton = document.createElement("button");
                decButton.innerText = "-";
                decButton.onclick = () => {
                    let storedCart = toMap(window.localStorage.getItem("cart"));
                    let vendorItems = storedCart.get(vendor);
                    if(vendorItems.get(itemName) > 0)
                    {
                        vendorItems.set(itemName, vendorItems.get(itemName) - 1);
                        window.localStorage.setItem("cart", toJson(storedCart));
                        saveCart(storedCart);
                    }
                }
                itemElement.appendChild(decButton);

                let priceElement = document.createElement("div");
                priceElement.innerHTML = "Price(per unit): " + itemPrice.get(vendor).get(itemName) 
                + "; Total Price: " + itemPrice.get(vendor).get(itemName)*groupedCart.get(vendor).get(itemName)
                + "<br><br>";
                itemElement.appendChild(priceElement);
                
                totalPrice += Number(itemPrice.get(vendor).get(itemName))*Number(groupedCart.get(vendor).get(itemName));

                vendorElement.appendChild(itemElement);
            }
            cartElement.appendChild(vendorElement);
        }
        document.getElementById("billing-div").innerText = "Final Price: " + totalPrice;
    }
}

async function saveCart(storedCart) {
    let cart = [];
    for(let [vendor, vendorItems] of storedCart)
    {
        for(let [itemName, amount] of vendorItems)
        {
            cart.push(new CartItem(vendor, itemName, amount));
        }
    }
    
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/cart", {
        method : 'POST',
        //mode : "same-origin",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
            },
        body : JSON.stringify(cart)
    });
    setUpCart();
}

document.addEventListener('DOMContentLoaded', async () => {

    setUpCart();
    
    document.getElementById("order-button").onclick = async () => {
        const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/order");
        window.localStorage.removeItem("cart");
        window.location.pathname = "/views/user pages/orders.html";
    };

});