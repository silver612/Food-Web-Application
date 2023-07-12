import { fetchAfterResponseCodeCheck } from "../middleware/checkResponseCode.js";
import { properties } from "../properties.js";


async function getUserLocations() {
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/locations");
    
        const responseBodyString = (await response.text());
        const locations = responseBodyString.substring(1, responseBodyString.length - 1)
                                    .split("},")
                                    .filter(stringObj => stringObj!=null && stringObj.length!=0)
                                    .map(stringObj => {
                                        if(stringObj[stringObj.length-1]!='}')
                                            stringObj = stringObj + "}";
                                        //console.log(stringObj, typeof(stringObj));
                                        return JSON.parse(stringObj);
                                    });
        if(locations==null || locations.length==0)
            return null;
        else
            return locations;
    
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
        map.addObject((currentMarker));
        map.setCenter(coord).setZoom(16);
        //console.log(currentMarker, currentMarker.a.lat, currentMarker.a.lng);
        mapLocationSelected(map, currentMarker.a.lat, currentMarker.a.lng);
      });
}

function mapLocationSelected(map, latitude, longitude){
    var actions_element = document.getElementById("actions");

    if(document.getElementById("address")==null){
        var address_field = document.createElement("input");
        address_field.setAttribute("id","address");
        address_field.setAttribute("placeholder", "Enter Address");
        actions_element.appendChild(address_field);
    }
    if(document.getElementById("address-button")==null){
        var save_button = document.createElement("button");
        save_button.setAttribute("id", "address-button");
        save_button.innerHTML = "Set Address";
        actions_element.appendChild(save_button);
    }
    document.getElementById("address-button").onclick = async () => {
        
        const address = document.getElementById("address").value;
        if(address==null)
            return;

        const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/locations", {
            method : 'PUT',
            //mode : "same-origin",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
            body : JSON.stringify({latitude, longitude, address})
        });

        setMapMarkers(map);
    };

}

async function setMapMarkers(map) {
    const userLocations = await getUserLocations();
    
    document.getElementById("addresses").replaceChildren();
    map.removeObjects(map.getObjects());
    const addressLabel = document.getElementById("address");
    if(addressLabel)
        addressLabel.remove();
    const addressButton = document.getElementById("address-button");
    if(addressButton)
        addressButton.remove();
    
    if(userLocations!=null)
    {
        document.getElementById("addresses").textContent = "Saved Locations are: \n"
        for(let userLocation in userLocations)
        {
            const lat = userLocations[userLocation].latitude;
            const lng = userLocations[userLocation].longitude;
            const add = userLocations[userLocation].address;
            let coord = new H.geo.Point(lat, lng);
            var addressItem = document.createElement("li");
            
            var addressName = document.createElement("p");
            addressName.innerText = add;
            addressName.onclick = () => {
                
                map.removeObjects(map.getObjects());
                var addressMarker = new H.map.Marker(coord);
                map.addObject(addressMarker);
            
                map.setCenter(coord).setZoom(16);
                mapLocationSelected(map, lat, lng);
            };

            var addressSelect = document.createElement("button");
            addressSelect.innerText = "Choose this location";
            addressSelect.onclick = () => {
                deliveryLocationSelected(coord, add);
            };
            
            var addressDelete = document.createElement("button");
            addressDelete.innerText = "Delete this location";
            addressDelete.onclick = async () => {
                const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/secure1/locations", {
                    method : 'DELETE',
                    //mode : "same-origin",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                        },
                    body : JSON.stringify({latitude: lat, longitude: lng, address: add})
                });
        
                setMapMarkers(map);
            };
            
            addressItem.append(addressName);
            addressItem.append(addressSelect);
            addressItem.append(addressDelete);

            document.getElementById("addresses").append(addressItem);
        }
    }
}

function deliveryLocationSelected(coord, add){
    window.localStorage["deliveryCoordinates"] = coord.lat + "%" + coord.lng + "%" + add;
    window.location.pathname = "/views/user pages/restaurants.html";
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

    document.getElementById("get-location-button").onclick = () => {
        getCurrentLocation(map);
    };
});


