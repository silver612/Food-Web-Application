package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.data.payloads.CartItem;

@Component
public interface CartService {

    public List<CartItem> getCart(String name);
    public void setCart(String name, List<CartItem> cart);
}
