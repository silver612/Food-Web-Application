document.addEventListener('DOMContentLoaded', ()=>{
    const username = window.localStorage.getItem("username");
    const role = window.localStorage.getItem("role");
    if(username==null || role==null)
        document.getElementById("links").innerHTML = "<a href=\"/views/signup.html\">Sign Up</a><br> <a href=\"/views/login.html\">Login</a>";
    else if(role=="VENDOR")
        window.location.pathname = "/views/vendor pages/home.html";
    else if(role=="USER")
        window.location.pathname = "/views/user pages/home.html";
});