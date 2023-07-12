package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.models.Item;
import com.example.demo.data.models.Vendor;
import com.example.demo.data.payloads.Location;
import com.example.demo.data.repository.VendorRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class PlaceListServiceImpl implements PlaceListService{
    
    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public List<Location> getPlaces(Float lat, Float lng){
        List<Vendor> vendorList = vendorRepository.findByCoord(lat, lng);
        if(vendorList.size()==0)
            return new ArrayList<Location>();
        else
            return vendorList.stream()
                    .map(vendor -> new Location(vendor.getLat(), vendor.getLng(), vendor.getName()))
                    .toList(); 
    }

    @Override
    public List<String> getPlaceMenu(String name){
        Optional<Vendor> vendorMaybe = vendorRepository.findByName(name);
        if(vendorMaybe.isEmpty())
            throw new ResourceNotFoundException(name);
        
        List<Item> items = vendorMaybe.get().getItemList();
        if(items.size()==0)
            return new ArrayList<String>();
        else
            return items.stream()
                    .map(item -> item.getName() + "=" + item.getPrice())
                    .toList();
    }
}
