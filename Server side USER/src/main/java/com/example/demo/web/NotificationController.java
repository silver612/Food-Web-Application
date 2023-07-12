package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.enums.Role;
import com.example.demo.service.NotifService;

@RestController
@RequestMapping("/secure")
public class NotificationController {
    
    @Autowired
    NotifService notifServiceImpl;

    @GetMapping("/userId")
    public ResponseEntity<String> getUserId(@CookieValue("username") String name, @CookieValue("role") Role role){
        return new ResponseEntity<String>(notifServiceImpl.getOneSignalId(name, role), HttpStatus.OK);
    }

    @PostMapping("/userId")
    public ResponseEntity<?> setUserId(@CookieValue("username") String name, @CookieValue("role") Role role, @RequestBody String userId){
        notifServiceImpl.setOneSignalId(name, role, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
