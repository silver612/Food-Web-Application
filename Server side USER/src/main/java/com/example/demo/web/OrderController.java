package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.payloads.OrderStatusChange;
import com.example.demo.service.OrderService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/secure")
public class OrderController {

    @Autowired
    OrderService orderServiceImpl;

    @GetMapping("/secure1/order")
    public ResponseEntity<?> createOrderFromCart(@CookieValue("username") String name){
        orderServiceImpl.createOrder(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/secure1/orders")
    public ResponseEntity<?> getOrdersForCustomer(@CookieValue("username") String name){
        return new ResponseEntity<>(orderServiceImpl.getOrdersForCustomer(name), HttpStatus.OK);
    }

    @GetMapping("/secure1/orders/{id}")
    public ResponseEntity<?> getOrder(@CookieValue("username") String name, @PathVariable("id") Integer id){
        return new ResponseEntity<>(orderServiceImpl.getOrderForCustomer(name, id), HttpStatus.OK);
    }

    @GetMapping("/secure2/orders")
    public ResponseEntity<?> getOrdersForVendor(@CookieValue("username") String name){
        return new ResponseEntity<>(orderServiceImpl.getOrdersForVendor(name), HttpStatus.OK);
    }
    
    @GetMapping("/secure2/orders/{id}")
    public ResponseEntity<?> getOrderSegmentForVendor(@CookieValue("username") String name, @PathVariable("id") Integer id){
        return new ResponseEntity<>(orderServiceImpl.getOrderSegmentForVendor(name, id), HttpStatus.OK);
    }

    @PostMapping("/secure2/orders/{id}")
    public ResponseEntity<?> setOrderSegmentStatus(@CookieValue("username") String name, @PathVariable("id") Integer id, @RequestBody OrderStatusChange orderStatusChange){
        if(orderServiceImpl.setOrderSegmentStatus(name, id, orderStatusChange.getOrderStatus()))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
