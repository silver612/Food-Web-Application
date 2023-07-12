import { fetchAfterResponseCodeCheck } from "../middleware/checkResponseCode.js";
import { properties } from "../properties.js";


async function getVendorLocation() {
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure2/location");
    
    const locationString = await response.text();
    if(locationString==null || locationString=="")
        return null;
    
    const location = JSON.parse(locationString);
    return location;
    
    return null;
}

function getCurrentLocation(map) {
    if(navigator.geolocation)
        navigator.geolocation.getCurrentPosition(function(position){
            let mylat=position.coords.latitude;
            let mylng=position.coords.longitude;
            //console.log(position.coords.accuracy)
            map.removeObjects(map.getObjects());
            var currentMarker = new H.map.Marker(new H.geo.Point(mylat, mylng));
            map.addObject(currentMarker);
            map.setCenter(new H.geo.Point(mylat, mylng)).setZoom(16);
            mapLocationSelected(map, mylat, mylng);
        }, () => {
            alert("Geopositioning failed.")
        }, {
            enableHighAccuracy: true
        });
    else
        alert("Browser does not support Geopositioning.")
}

function setupClickListener(map){
    map.addEventListener('tap', function (evt) {
        var coord = map.screenToGeo(evt.currentPointer.viewportX,
                evt.currentPointer.viewportY);
        var currentMarker = new H.map.Marker(coord);
        map.removeObjects(map.getObjects());
        map.addObject(currentMarker);
        map.setCenter(coord).setZoom(16);
        //console.log(currentMarker, currentMarker.a.lat, currentMarker.a.lng);
        mapLocationSelected(map, currentMarker.a.lat, currentMarker.a.lng);
      });
}

function mapLocationSelected(map, latitude, longitude){
    var actions_element = document.getElementById("actions");

    if(document.getElementById("address-button")==null){
        var save_button = document.createElement("button");
        save_button.setAttribute("id", "address-button");
        save_button.innerHTML = "Set Pin Location as Restaurant Location";
        actions_element.appendChild(save_button);
    }
    document.getElementById("address-button").onclick = async () => {
        
        const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure2/location", {
            method : 'POST',
            //mode : "same-origin",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
            body : JSON.stringify({latitude, longitude, address:""})
        });
        
        setMapMarkers(map)

    };

}

async function setMapMarkers(map) {
    const userLocation = await getVendorLocation();
    //console.log(userLocations);

    if(userLocation!=null)
    {
        document.getElementById("addresses").innerHTML = "Saved Location is: <br>"
        
        const lat = userLocation.latitude;
        const lng = userLocation.longitude;
        let coord = new H.geo.Point(lat, lng);
        
        var addressItem = document.createElement("button");
        addressItem.innerHTML = "Restaurant Location";
        addressItem.addEventListener('click', () => {
            
            map.removeObjects(map.getObjects());
            var addressMarker = new H.map.Marker(coord);
            map.addObject(addressMarker);
        
            map.setCenter(coord).setZoom(16);
            mapLocationSelected(map, lat, lng);
        });
        
        document.getElementById("addresses").append(addressItem);   
    }
    else
        document.getElementById("addresses").innerHTML = "No location saved. <br>"
}

document.addEventListener('DOMContentLoaded', async () => {

    var platform = new H.service.Platform({
        'apikey': properties.map_api_key
      });
      
    // Obtain the default map types from the platform object:
    var defaultLayers = platform.createDefaultLayers();
    
    // Instantiate (and display) a map object:
    let map = new H.Map(
        document.getElementById('map'),
        defaultLayers.vector.normal.map,
        {
          zoom: 2,
          center: { lat: 52.5, lng: 13.4 }  // default
        });
    
    setMapMarkers(map);
    
    var behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));
    
    setupClickListener(map);

    document.getElementById("get-location-button").addEventListener('click', () => {
        getCurrentLocation(map);
    });
});


