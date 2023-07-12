package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.payloads.Location;
import com.example.demo.service.LocationService;

@RestController
@RequestMapping("/secure/secure1")
public class LocationController {

    @Autowired
    LocationService locationServiceImpl;

    @GetMapping("/locations")
    public ResponseEntity<List<Location> > getLocations(@CookieValue("username") String username)
    {
        return new ResponseEntity<List<Location> >(locationServiceImpl.getLocations(username), HttpStatus.OK);
    }

    @PutMapping("/locations")
    public ResponseEntity<?> setLocations(@CookieValue("username") String username, @RequestBody Location location)
    {
        locationServiceImpl.setLocation(username, location);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/locations")
    public ResponseEntity<?> deleteLocation(@CookieValue("username") String username, @RequestBody Location location)
    {
        locationServiceImpl.deleteLocation(username, location);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
