import {UserAuth} from "./model/UserAuth.js";
import { properties } from "./properties.js";

let role = "USER";

function switchRole(){
    if(role=="USER")
    {
        role="VENDOR";
        document.getElementById("signup-type").innerText = "Vendor Account Creation";
        document.getElementById("role-switch").innerText = "For User Account Creation";
    }
    else if(role=="VENDOR")
    {
        role="USER";
        document.getElementById("signup-type").innerText = "User Account Creation";
        document.getElementById("role-switch").innerText = "For Vendor Account Creation";
    }
}

async function checkPasswordReq(password){
    let pattern = /^\w{6,15}$/;
    return password.match(pattern)!=null;
}

async function createUser(username, password){
    
    let password_qualified = await checkPasswordReq(password);
    if(!password_qualified)
        return {success: false, message: "Password does not meet criteria"}

    const userAuth = new UserAuth(username, password, role);

    const response = await fetch(properties.server_url + "/signup", {
        method : 'POST',
        //mode : "same-origin",
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
            },
        body : JSON.stringify(userAuth)
    });
    
    const responseMessage = await response.text();
    if(responseMessage=="ALREADY_EXISTS")
        return {success: false, message: "Username already exists"};
    else
        return {success: true, message: ""};
}

document.addEventListener('DOMContentLoaded', ()=>{
    
    document.getElementById("role-switch").addEventListener("click", () => {switchRole()});

    let button_element = document.getElementById("signup-button");
    button_element.addEventListener("click", async () =>{

        const username = document.getElementById("uname").value;
        const password = document.getElementById("psw").value;
        let {success, message} = await createUser(username, password);
        if(success)
        {
            window.localStorage.setItem("username", username);
            window.localStorage.setItem("role", role);
            if(role=="USER")
                window.location.pathname = "/views/user pages/home.html";
            else if(role=="VENDOR")
                window.location.pathname = "/views/vendor pages/location.html";
        }
        else 
            document.getElementById("notify").innerText = message;
    });
});