import {properties} from "../properties.js"
import {fetchAfterResponseCodeCheck} from "../middleware/checkResponseCode.js"

async function calculateDistance(userLat, userLng, placeLat, placeLng) {
    
    var platform = new H.service.Platform({
        'apikey' : properties.map_api_key
    });

    var routingParams = {
        'routingMode' : 'fast',
        'transportMode' : 'car',
        'origin' : userLat + "," + userLng,
        'destination' : placeLat + "," + placeLng,
        'return' : 'summary'
    };

    var routeLength = 0.0;
    var onsuccess = (result) => {
        if(!result.routes.length)
        {
            routeLength = undefined;
            return;
        }
        
        result.routes[0].sections.forEach((section)=>{
            routeLength = routeLength + section.summary.length / 1000;
        });
    };

    var router = platform.getRoutingService(null, 8);
    await router.calculateRoute(routingParams, onsuccess, function(error) {
        routeLength = undefined;
    });
    
    if(routeLength==undefined)
        return "Route not calculated";
    else 
        return routeLength + " km";
}

async function setupRestaurants(lat, lng) {
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/places?lat=" + lat + "&lng=" + lng);
    
    let placeListElement = document.getElementById("restaurants-div");
    placeListElement.replaceChildren();
    
    if(response.body=='')
        return;

    const places = await response.json();
    
    for(let place of places)
    {
        let restaurantElement = document.createElement("p");
        restaurantElement.innerText = place.address + ": " + await calculateDistance(lat, lng, place.latitude, place.longitude);
        restaurantElement.onclick = () => {
            window.localStorage["place"] = place.address;
            window.location.pathname = "/views/user pages/restaurant.html";
        };
        document.getElementById("restaurants-div").append(restaurantElement);
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    if(window.localStorage["deliveryCoordinates"]==null)
    {
        document.getElementById("address").innerHTML = "You have not selected an address. <a href=\"./location.html\"> Choose your address</a>";
        return;
    }

    var lat = parseFloat(window.localStorage["deliveryCoordinates"].split("%")[0]);
    var lng = parseFloat( window.localStorage["deliveryCoordinates"].split("%")[1]);
    var add =  window.localStorage["deliveryCoordinates"].split("%")[2];
    document.getElementById("address").innerHTML = add + "<br> <a href=\"./location.html\">Choose a different address</a>";

    setupRestaurants(lat, lng);
});