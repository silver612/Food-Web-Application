package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.models.Customer;
import com.example.demo.data.payloads.Location;
import com.example.demo.data.repository.CustomerRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class LocationServiceImpl implements LocationService {
    
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<Location> getLocations(String username)
    {
        Optional<Customer> userMaybe = customerRepository.findByName(username);
        if(userMaybe.isEmpty())
            throw new ResourceNotFoundException(username);
        Customer user = userMaybe.get();
        String locationsString = user.getLocations();
        if(!locationsString.equals(""))
            return Arrays.stream(locationsString.split(";")).map(entry -> new Location(entry)).toList();
        else
            return new ArrayList<>();
    }

    @Override 
    public void setLocation(String username, Location newLocation)
    {   
        Optional<Customer> userMaybe = customerRepository.findByName(username);
        if(userMaybe.isEmpty())
            throw new ResourceNotFoundException(username);
        Customer user = userMaybe.get();
        String locationsString = user.getLocations();
        
        List<Location> locations = new ArrayList<Location>();
        if(!locationsString.equals(""))
            locations = Arrays.stream(locationsString.split(";"))
                                    .map(entry -> new Location(entry))
                                    .filter(location -> !location.toString().equals(newLocation.toString()))
                                    .collect(Collectors.toList());
        
        locations.add(newLocation);
        user.setLocations(locations.stream()
                            .map(location -> location.toString()+"="+location.getAddress())
                            .collect(Collectors.joining(";")));
        customerRepository.save(user);
    }

    @Override
    public void deleteLocation(String username, Location locationToRemove)
    {
        Optional<Customer> userMaybe = customerRepository.findByName(username);
        if(userMaybe.isEmpty())
            throw new ResourceNotFoundException(username);
        Customer user = userMaybe.get();
        String locationsString = user.getLocations();
        List<Location> locations;
        if(!locationsString.equals(""))
        {
            locations = Arrays.stream(locationsString.split(";"))
                                    .map(entry -> new Location(entry))
                                    .filter(location -> !location.toString().equals(locationToRemove.toString()))
                                    .collect(Collectors.toList());
            if(locations.size()!=0)
                user.setLocations(locations.stream()
                                .map(location -> location.toString()+"="+location.getAddress())
                                .collect(Collectors.joining(";")));
            else
                user.setLocations("");
            customerRepository.save(user);
        }
    }
}
