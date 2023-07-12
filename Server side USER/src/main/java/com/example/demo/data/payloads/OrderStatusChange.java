package com.example.demo.data.payloads;

import com.example.demo.data.enums.OrderStatus;

public class OrderStatusChange {
    
    private OrderStatus orderStatus;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    
}
