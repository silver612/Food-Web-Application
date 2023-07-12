package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.models.Item;
import com.example.demo.data.payloads.ItemChange;
import com.example.demo.data.payloads.Location;
import com.example.demo.service.VendorService;

@RestController
@RequestMapping("/secure/secure2")
public class VendorController {
    
    @Autowired
    VendorService vendorServiceImpl;

    @GetMapping(path = "/location")
    @ResponseBody
    public Location getCoord(@CookieValue("username") String username) {
        Location location = vendorServiceImpl.getCoord(username);
        if(location.getLatitude()==null || location.getLongitude()==null)
            return null;
        else
            return location;
    }

    @PostMapping(path = "/location")
    public ResponseEntity<?> setCoord(@CookieValue("username") String username, @RequestBody Location location) {
        vendorServiceImpl.setCoord(username, location);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/items")
    @ResponseBody
    public List<Item> getItems(@CookieValue("username") String username) {
        return vendorServiceImpl.getItemList(username);
    }

    @PostMapping(path = "/items")
    public ResponseEntity<?> setItems(@CookieValue("username") String username, @RequestBody List<ItemChange> itemChangeList) {
        vendorServiceImpl.setItemList(username, itemChangeList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
