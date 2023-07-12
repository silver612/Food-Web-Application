package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.data.payloads.Location;

@Component
public interface PlaceListService {
    
    public List<Location> getPlaces(Float lat, Float lng);
    public List<String> getPlaceMenu(String name); 
}
