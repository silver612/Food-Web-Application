package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.payloads.CartItem;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/secure/secure1")
public class CartController {
    
    @Autowired
    private CartService cartServiceImpl;

    @GetMapping("/cart")
    public ResponseEntity<List<CartItem> > getCart(@CookieValue("username") String name)
    {
        return new ResponseEntity<List<CartItem>>(cartServiceImpl.getCart(name), HttpStatus.OK);
    }

    @PostMapping("/cart")
    public ResponseEntity<?> setCart(@CookieValue("username") String name, @RequestBody List<CartItem> cart)
    {
        cartServiceImpl.setCart(name, cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
