# Desciption

This is the code for a food web application. This project consists of 2 segments: Client side and Server side. The Client-side is responsible for generating the webpages which are visible to users. The server side exposes several endpoints which are used by the client side. This website consists of separate portals for use by vendors and customers. A vendor account cannot be used for customer specific actions such as ordering. Similarly, only a vendor can do vendor specific actions such as setting a menu, receiving orders, etc. 

This code uses [HERE maps](https://developer.here.com/) for location related services such as obtaining user location and showing nearby restaurants.

This code also uses [OneSignal](https://onesignal.com/) for web push notifications.

The server side code is written using JAVA and Spring Boot.
The client side code is written using HTML, CSS and Vanilla JS.

# Instructions for set up

1. Make sure your device is able to run JAVA. If not, install JAVA.

2. Setup the server side code: All files under the folder [Server Side USER](./Server%20side%20USER) need to be on the device that is your server. The folder structure must remain the same. Run [DemoApplication.java](./Server%20side%20USER/src/main/java/com/example/demo/DemoApplication.java). Note the URL on which you will be exposing the endpoints. I used http://localhost:8080 for code development. This url will be needed while setting up client side code.

3. Setup the client side code: All files under the folder [Client side USER](./Client%20side%20USER/) need to be available to the user. This can be done by hosting on the same device that user has or making it accessible to the user via a URL. I used http://localhost:5500 for code development. The folder structure must remain the same. Assign the URL obtained in step 2 to 'server_url' in [properties.js](./Client%20side%20USER/src/properties.js).

4. The user may start by accessing [home.html](./Client%20side%20USER/views/home.html), which is the landing page.

### Additional information 

The client side is a functional front end with no emphasis on UI/UX. It only exists to showcase the working of the server side code. 

Details about working of the server side code can be found in [Working Details.md](./Server%20side%20USER/Working%20Details.md). That file also includes current flaws as well as possible directions of expansion of the code.