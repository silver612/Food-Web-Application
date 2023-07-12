
export async function fetchAfterResponseCodeCheck(resource, options){

    let response;

    if(options==undefined)
        response = await fetch(resource,{
            method: 'GET',
            credentials: 'include'
        });
    else
    {
        options.credentials = 'include'
        response = await fetch(resource, options);
    }
    const responseStatus = response.status;
    
    if(responseStatus!=200) 
    {
        const responseText = await response.text();
        console.log(responseText);
        if(responseText=="Session Id Mismatch" || responseText=="Invalid Role")
            window.location.pathname = "/views/login.html";
        else 
            window.location.href = "/views/error.html?message=" + responseText;
        await new Promise(r => setTimeout(r, 10000)); // blocking function ending till code loads
    }
    else
        return response;
}