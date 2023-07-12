package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.data.models.Item;
import com.example.demo.data.payloads.ItemChange;
import com.example.demo.data.payloads.Location;

@Component
public interface VendorService {
    
    public Location getCoord(String username);
    public void setCoord(String username, Location location);
    public List<Item> getItemList(String username);
    public void setItemList(String username, List<ItemChange> newItemList);
}
