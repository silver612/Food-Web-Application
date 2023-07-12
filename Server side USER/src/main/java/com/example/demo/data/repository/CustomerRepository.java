package com.example.demo.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.data.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

    @Query(value = "SELECT * FROM customer WHERE name = ?1 LIMIT 1", nativeQuery = true)
    public Optional<Customer> findByName(String name);

}
