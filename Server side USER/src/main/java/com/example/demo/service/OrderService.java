package com.example.demo.service;

import java.util.List;

import com.example.demo.data.enums.OrderStatus;
import com.example.demo.data.payloads.OrderDetails;
import com.example.demo.data.payloads.OrderShort;

public interface OrderService {

    public void createOrder(String name);
    public List<OrderShort> getOrdersForCustomer(String name);
    public OrderDetails getOrderForCustomer(String name, Integer id);
    public List<OrderShort> getOrdersForVendor(String name);
    public OrderDetails getOrderSegmentForVendor(String name, Integer id);
    public boolean setOrderSegmentStatus(String name, Integer id, OrderStatus newStatus);
}