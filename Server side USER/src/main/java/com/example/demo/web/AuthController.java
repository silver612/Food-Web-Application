package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.enums.Role;
import com.example.demo.data.payloads.UserAuth;
import com.example.demo.service.AuthService;

@RestController
public class AuthController {
    
    @Autowired
    AuthService authServiceImpl;

    @PatchMapping("/auth") // should be GET but needs body
    public ResponseEntity<?> checkUser(@RequestBody UserAuth userAuth)
    {
        String sessionId = authServiceImpl.checkValidityOfPassword(userAuth);
        if(sessionId!="Incorrect Password" && sessionId!="")
        {
            HttpCookie cookie1 = ResponseCookie.from("sessionId", sessionId).maxAge(3600).path("/secure").secure(true).httpOnly(true).build();
            HttpCookie cookie2 = ResponseCookie.from("username", userAuth.getUsername()).maxAge(3600).path("/secure").secure(true).httpOnly(true).build();
            HttpCookie cookie3 = ResponseCookie.from("role", userAuth.getRole().toString()).maxAge(3600).path("/secure").secure(true).httpOnly(true).build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
            headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
            headers.add(HttpHeaders.SET_COOKIE, cookie3.toString());
            return new ResponseEntity<String>(sessionId, headers, HttpStatus.OK);
        }
        else
            return new ResponseEntity<String>(sessionId, HttpStatus.UNAUTHORIZED);
    }  

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody UserAuth userAuth)
    {
        String response = authServiceImpl.createUser(userAuth);
        if(response=="ALREADY_EXISTS")
            return new ResponseEntity<String>(response, HttpStatus.OK);
        else 
        {
            HttpCookie cookie1 = ResponseCookie.from("sessionId", response).maxAge(3600).path("/secure").secure(true).httpOnly(true).build();
            HttpCookie cookie2 = ResponseCookie.from("username", userAuth.getUsername()).maxAge(3600).path("/secure").secure(true).httpOnly(true).build();
            HttpCookie cookie3 = ResponseCookie.from("role", userAuth.getRole().toString()).maxAge(3600).path("/secure").secure(true).httpOnly(true).build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
            headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
            headers.add(HttpHeaders.SET_COOKIE, cookie3.toString());
            return new ResponseEntity<String>(response, headers, HttpStatus.OK);
        }
    }

    @PutMapping("/secure/auth")
    public ResponseEntity<?> clearCookies()
    {
        HttpCookie cookie1 = ResponseCookie.from("sessionId", null).maxAge(0).path("/secure").secure(true).httpOnly(true).build();
        HttpCookie cookie2 = ResponseCookie.from("username", null).maxAge(0).path("/secure").secure(true).httpOnly(true).build();
        HttpCookie cookie3 = ResponseCookie.from("role", null).maxAge(3600).path("/secure").secure(true).httpOnly(true).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie3.toString());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @DeleteMapping("/secure/deleteAccount")
    public ResponseEntity<?> deleteUser(@CookieValue("username") String username, @CookieValue("role") String roleString)
    {
        Role role = Role.valueOf(roleString);
        authServiceImpl.deleteUser(username, role);
        HttpCookie cookie1 = ResponseCookie.from("sessionId", null).maxAge(0).path("/secure").secure(true).httpOnly(true).build();
        HttpCookie cookie2 = ResponseCookie.from("username", null).maxAge(0).path("/secure").secure(true).httpOnly(true).build();
        HttpCookie cookie3 = ResponseCookie.from("role", null).maxAge(0).path("/secure").secure(true).httpOnly(true).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie3.toString());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/secure/updateAccount/username")
    public ResponseEntity<?> updateUsername(@CookieValue("username") String username, @CookieValue("role") String roleString, @RequestBody String newUsername)
    {
        Role role = Role.valueOf(roleString);
        String result = authServiceImpl.updateUsername(username, newUsername, role);
        if(result=="ALREADY_EXISTS")
            return new ResponseEntity<String>(result, HttpStatus.OK);
        
        HttpCookie cookie2 = ResponseCookie.from("username", newUsername).maxAge(3600).path("/secure").secure(true).httpOnly(true).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
        return new ResponseEntity<String>(result, headers, HttpStatus.OK);
    }

    @PutMapping("/secure/updateAccount/password")
    public ResponseEntity<?> updatePassword(@CookieValue("username") String username, @CookieValue("role") String roleString, @RequestBody UserAuth userAuth)
    {
        Role role = Role.valueOf(roleString);
        boolean result = authServiceImpl.updatePassword(username, userAuth, role);
        if(result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<String>("Incorrect Password", HttpStatus.UNAUTHORIZED);
    }

}
