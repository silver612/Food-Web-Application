package com.example.demo.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.data.models.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    
    public Optional<Order> findById(Integer id);
}
