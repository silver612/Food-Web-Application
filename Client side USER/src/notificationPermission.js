import {fetchAfterResponseCodeCheck} from "./middleware/checkResponseCode.js"
import {properties} from "./properties.js"

OneSignal.push(function() {
    OneSignal.isPushNotificationsEnabled(function(isEnabled) {
      if (isEnabled){
        
        OneSignal.getUserId( async function(userId) {
          const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/userId", {
                method : 'POST',
                //mode : "same-origin",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                    },
                body : userId
            });
        });
      }
      else
        alert("Please subscribe to notifications in bottom right to receive updates");   
    });
  });