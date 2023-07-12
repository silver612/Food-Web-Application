package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.data.payloads.Location;

@Component
public interface LocationService {
    public List<Location> getLocations(String username);
    public void setLocation(String username, Location location);
    public void deleteLocation(String username, Location location);
}
