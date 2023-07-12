package com.example.demo.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.data.models.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
    
    @Query(value = "select * from item where name = ?2 and id in (" 
                + "    select item_list_id from vendor_item_list where vendor_id in (" 
                + "        select id from vendor where name = ?1"
                + "    )"
                + ") LIMIT 1;", nativeQuery = true)
    public Optional<Item> findItemByNameAndVendor(String name, String itemName);
}
