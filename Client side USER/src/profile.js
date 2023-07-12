import { properties } from "./properties.js";
import { fetchAfterResponseCodeCheck } from "./middleware/checkResponseCode.js"
import { UserAuth } from "./model/UserAuth.js";

async function checkPasswordReq(password){
    let pattern = /^\w{6,15}$/;
    return password.match(pattern)!=null;
}


async function deleteAccount() {
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/deleteAccount",{
        method : 'DELETE',
        //mode : "same-origin",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
            }
    })
    window.localStorage.clear();
    window.location.pathname = "./views/home.html";
}

async function updateUsername(newUsername) {
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/updateAccount/username",{
        method : 'PUT',
        //mode : "same-origin",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
            },
        body: newUsername
    })
    
    window.localStorage.setItem("username", newUsername);
    window.location.reload();
    
}

async function updatePassword(userAuth) {

    const passwordQualified = await checkPasswordReq(userAuth.newPassword);
    
    if(!passwordQualified)
    {
        document.getElementById("notify").innerText = "Password does not meet requirements. Password must be between 6 to 16 characters which contain only characters, numeric digits and underscore."
        return;
    }

    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/updateAccount/password",{
        method : 'PUT',
        //mode : "same-origin",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
            },
        body: JSON.stringify(userAuth)
    })
    
    window.location.reload();
}

async function logoutUser(){
    const response = await fetchAfterResponseCodeCheck(properties.server_url + "/secure/auth",{
        method : 'PUT',
        //mode : "same-origin",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
            }
    })
    
    window.localStorage.clear();
    window.location.pathname = "./views/home.html";
    
}

document.addEventListener('DOMContentLoaded', ()=>{

    document.getElementById("static-text").innerText = "Hi, " + window.localStorage.getItem("username") + ".";

    document.getElementById("uname-button").addEventListener("click", () => {
        const newUsername = document.getElementById("uname").value;
        updateUsername(newUsername);
    });

    document.getElementById("psw-button").addEventListener("click", () => {
        const password = document.getElementById("psw").value;
        const newPassword = document.getElementById("psw-new").value;
        const userAuth = new UserAuth("", password, null, "", newPassword)
        updatePassword(userAuth);
    });

    document.getElementById("delete-button").addEventListener("click", () => {
        deleteAccount();
    });

    document.getElementById("logout-button").addEventListener("click", () => {
        logoutUser();
    })
});