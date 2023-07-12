package com.example.demo.data.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Customer extends User{

    private String locations; 

    private String cart;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private List<Order> orders;

    public Customer() {
        locations = "";
        cart = "";
    }

    public String getLocations() {
        return locations;
    }

    public String getCart() {
        return cart;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
