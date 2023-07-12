package com.example.demo.data.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Vendor extends User{

    private Float lat, lng;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> itemList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vendor")
    List<OrderSegment> orderSegments;

    public Vendor() {
        itemList = new ArrayList<Item>();
    }

    public Float getLat() {
        return lat;
    }

    public Float getLng() {
        return lng;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public List<OrderSegment> getOrderSegments() {
        return orderSegments;
    }

    public void setOrderSegments(List<OrderSegment> orderSegments) {
        this.orderSegments = orderSegments;
    }
}
