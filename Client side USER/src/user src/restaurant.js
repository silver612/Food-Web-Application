import {properties} from "../properties.js"
import {fetchAfterResponseCodeCheck} from "../middleware/checkResponseCode.js"
import { toJson, toMap } from "../utility/mapToJson.js";
import { CartItem } from "../model/CartItem.js";

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
}

async function setUpItems(placeName){
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/place?name=" + placeName);
    
    const items = await response.json();
    for(let item of items)
    {
        let itemElement = document.createElement("p")
        itemElement.innerText = item;

        let cartButton = document.createElement("button");
        cartButton.innerText = "Add to cart";
        cartButton.onclick = () => {
            let storedCart ;
            if(window.localStorage.getItem("cart")==null)
                storedCart = new Map();
            else
                storedCart = toMap(window.localStorage.getItem("cart"));
            let cartVendor = storedCart.get(placeName);
            if(cartVendor==undefined)
            {
                storedCart.set(placeName, new Map());
                cartVendor = storedCart.get(placeName);
            }
            cartVendor.set(item.split("=")[0], 1);
            window.localStorage.setItem("cart", toJson(storedCart));
            saveCart(storedCart);
        }
        itemElement.append(cartButton);

        document.getElementById("items-div").append(itemElement);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    var placeName = window.localStorage["place"];
    if(placeName==undefined)
        window.location.pathname = "/views/user pages/restaurants.html";

    setUpItems(placeName);
});

