package com.example.demo.data.payloads;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.data.enums.OrderStatus;

public class OrderDetails {
    
    private Integer id;
    private List<String> vendors;
    private List<Float> prices;
    private List<OrderStatus> statuses;
    private List<List<Integer> > qtyLists;
    private List<List<String> > itemLists;

    public OrderDetails(){
        vendors = new ArrayList<>();
        prices = new ArrayList<>();
        itemLists = new ArrayList<>();
        qtyLists = new ArrayList<>();
        statuses = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public List<String> getVendors() {
        return vendors;
    }
    public void setVendors(List<String> vendors) {
        this.vendors = vendors;
    }
    public List<Float> getPrices() {
        return prices;
    }
    public void setPrices(List<Float> prices) {
        this.prices = prices;
    }
    public List<List<String>> getItemLists() {
        return itemLists;
    }
    public void setItemLists(List<List<String> > itemLists) {
        this.itemLists = itemLists;
    }

    public List<List<Integer> > getQtyLists() {
        return qtyLists;
    }

    public void setQtyLists(List<List<Integer> > qtys) {
        this.qtyLists = qtys;
    }
    
    public List<OrderStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<OrderStatus> statuses) {
        this.statuses = statuses;
    }

    
}
