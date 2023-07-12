import {UserAuth} from "./model/UserAuth.js";
import { properties } from "./properties.js";

let role = "USER";

function switchRole(){
    if(role=="USER")
    {
        role="VENDOR";
        document.getElementById("login-type").innerText = "Vendor Login";
        document.getElementById("role-switch").innerText = "For User Login";
    }
    else if(role=="VENDOR")
    {
        role="USER";
        document.getElementById("login-type").innerText = "User Login";
        document.getElementById("role-switch").innerText = "For Vendor Login";
    }
}

async function sendLoginRequest(username, password){
    
    const userAuth = new UserAuth(username, password, role);

    const response = await fetch(properties.server_url + "/auth", {
        method : 'PATCH',
        //mode : "same-origin",
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
        body : JSON.stringify(userAuth)
    });
    const responseStatus = response.status;
    if(responseStatus == 200)
        return {success: true, message : ""};
    
    const responseMessage = await response.text();
    return {success: false, message: responseMessage};
}

document.addEventListener('DOMContentLoaded', ()=>{

    document.getElementById("role-switch").addEventListener("click", () => {switchRole()});

    let button_element = document.getElementById("login-button");
    button_element.addEventListener("click", async () =>{

        const username = document.getElementById("uname").value;
        const password = document.getElementById("psw").value;
        let {success, message} = await sendLoginRequest(username, password);
        if(success)
        {
            window.localStorage.setItem("username", username);
            window.localStorage.setItem("role", role);
            if(role=="USER")
                window.location.pathname = "/views/user pages/home.html";
            else if(role=="VENDOR")
                window.location.pathname = "/views/vendor pages/home.html";
        }
        else 
            document.getElementById("notify").innerText = message;
    });
});