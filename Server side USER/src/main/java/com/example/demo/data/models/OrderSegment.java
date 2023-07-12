package com.example.demo.data.models;

import java.util.Map;

import com.example.demo.data.enums.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderSegment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Vendor vendor;

    @ManyToOne
    private Order order;

    @ElementCollection
    @Column(name = "Quantity")
    private Map<Item, Integer> items;
    
    private OrderStatus status;

    public OrderSegment(Vendor vendor, Map<Item, Integer> items) {
        this.vendor = vendor;
        this.items = items;
        status = OrderStatus.ASKING;
    }

    public OrderSegment() {
    }

    public Integer getId() {
        return id;
    }
    
    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
