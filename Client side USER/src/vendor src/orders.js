import { fetchAfterResponseCodeCheck } from "../middleware/checkResponseCode.js";
import { properties } from "../properties.js";

async function getOrders(){
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure2/orders");
    return await response.json();
}

async function getOrderDetails(id){
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure2/orders/" + id);
    return await response.json();
}

async function setOrderSegmentStatus(orderId, newStatus){
    const bodyObject = {newStatus};
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure2/orders/" + orderId, {
        method : 'POST',
            //mode : "same-origin",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
            body : "{\"orderStatus\":\"" + newStatus + "\"}" 
    });
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

    var vendorElement = document.createElement("h3");
    vendorElement.innerText = order.vendors[0];
    orderElement.appendChild(vendorElement);

    var itemsElement = document.createElement("ol");
    for(var innerIndex in order.itemLists[0])
    {
        var itemElement = document.createElement("li");
        itemElement.innerText = order.itemLists[0][innerIndex] + " : " + order.qtyLists[0][innerIndex];
        itemsElement.appendChild(itemElement);
    }
    orderElement.appendChild(itemsElement);

    var priceElement = document.createElement("p");
    priceElement.innerText = "Price: " + order.prices[0];
    orderElement.appendChild(priceElement);

    orderElement.appendChild(document.createElement("br"));

    var statusElement = document.createElement("div");
    if(order.statuses[0]==="ASKING")
    {
        var acceptButton = document.createElement("button");
        acceptButton.innerText = "Accept Order";
        acceptButton.onclick = async () => {
            await setOrderSegmentStatus(id, "INPROGRESS");
            setUpOrderDetails(id);
        };
        statusElement.append(acceptButton);
        
        var rejectButton = document.createElement("button");
        rejectButton.innerText = "Reject Order";
        rejectButton.onclick = async () => {
            await setOrderSegmentStatus(id, "REJECTED");
            setUpOrderDetails(id);
        }
        statusElement.append(rejectButton);
    }
    else if(order.statuses[0]==="REJECTED")
    {
        statusElement.innerHTML = "Order rejected";
    }
    else if(order.statuses[0]==="INPROGRESS")
    {
        statusElement.innerText = "Food being prepared";
        var transportButton = document.createElement("button");
        transportButton.innerText = "Sent for delivery";
        transportButton.onclick = async () => {
            await setOrderSegmentStatus(id, "TRANSPORTING");
            setUpOrderDetails(id);
        };
        statusElement.append(transportButton);
    }
    else if(order.statuses[0]==="TRANSPORTING")
    {
        statusElement.innerHTML = "Out for Delivery";
        var deliveryButton = document.createElement("button");
        deliveryButton.innerText = "Delivery Completed";
        deliveryButton.onclick = async () => {
            await setOrderSegmentStatus(id, "DELIVERED");
            setUpOrderDetails(id);
        };
        statusElement.append(deliveryButton);
    }
    else if(order.statuses[0]==="DELIVERED")
    {
        statusElement.innerHTML = "Delivered"
    }
    else
        window.location.pathname = "/views/error.html?message=Invalid order status for order id " + id;
    orderElement.appendChild(statusElement);

}

document.addEventListener('DOMContentLoaded', () => {

    setUpOrders();
});