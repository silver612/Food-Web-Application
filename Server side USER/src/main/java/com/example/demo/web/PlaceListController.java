package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.payloads.Location;
import com.example.demo.service.PlaceListService;

@RestController
@RequestMapping("/secure/secure1")
public class PlaceListController {
    
    @Autowired
    private PlaceListService placeListServiceImpl;

    @GetMapping("/places")
    public ResponseEntity<List<Location> > getPlaces(@RequestParam Float lat, @RequestParam Float lng)
    {
        List<Location> res = placeListServiceImpl.getPlaces(lat, lng);
        if(res.isEmpty())
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<List<Location> >(res, HttpStatus.OK);
    }

    @GetMapping("/place")
    public ResponseEntity<List<String> > getPlaceMenu(@RequestParam String name)
    {        
        List<String> res = placeListServiceImpl.getPlaceMenu(name);
        if(res.isEmpty())
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<List<String> >(res, HttpStatus.OK);
    }
}
