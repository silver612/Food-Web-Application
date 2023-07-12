package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.data.models.Item;
import com.example.demo.data.models.Vendor;
import com.example.demo.data.payloads.ItemChange;
import com.example.demo.data.payloads.Location;
import com.example.demo.data.repository.VendorRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Component
public class VendorServiceImpl implements VendorService{
    
    @Autowired
    VendorRepository vendorRepository;

    @Override 
    public Location getCoord(String username) {
        Optional<Vendor> vendorMaybe = vendorRepository.findByName(username);
        if(vendorMaybe.isEmpty())
            throw new ResourceNotFoundException(username);
        
        return new Location(vendorMaybe.get().getLat(), vendorMaybe.get().getLng());
    }

    @Override
    public void setCoord(String username, Location location) {
        Optional<Vendor> vendorMaybe = vendorRepository.findByName(username);
        if(vendorMaybe.isEmpty())
            throw new ResourceNotFoundException(username);
        
        Vendor vendor = vendorMaybe.get();

        vendor.setLat(location.getLatitude());
        vendor.setLng(location.getLongitude());

        vendorRepository.save(vendor);
    }

    @Override
    public List<Item> getItemList(String username) {
        Optional<Vendor> vendorMaybe = vendorRepository.findByName(username);
        if(vendorMaybe.isEmpty())
            throw new ResourceNotFoundException(username);
        
        return vendorMaybe.get().getItemList();
    }

    @Override
    public void setItemList(String username, List<ItemChange> itemChanges) {
        Optional<Vendor> vendorMaybe = vendorRepository.findByName(username);
        if(vendorMaybe.isEmpty())
            throw new ResourceNotFoundException(username);

        Vendor vendor = vendorMaybe.get();
        List<Item> items = vendor.getItemList();
        for(ItemChange itemChange : itemChanges)
        {
            Optional<Integer> itemMatchIndex = items.stream().filter(item -> item.getName().equals(itemChange.getName())).map(item -> items.indexOf(item)).findFirst();
            if(itemChange.getNewName().equals("")) // deletion of item
            {
                if(itemMatchIndex.isEmpty())
                    return;
                items.remove(itemMatchIndex.get().intValue());
            }
            else // updation of item
            {
                if(itemMatchIndex.isEmpty())
                {
                    Item newItem = new Item();
                    newItem.setName(itemChange.getNewName());
                    newItem.setPrice(itemChange.getNewPrice());
                    items.add(newItem);
                }
                else  
                {
                    Item item = items.get(itemMatchIndex.get());
                    item.setName(itemChange.getNewName());
                    item.setPrice(itemChange.getNewPrice());
                    items.set(itemMatchIndex.get(), item);
                }
            }
        }
        vendorRepository.save(vendor);
    }
}
