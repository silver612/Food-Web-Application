package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.models.Customer;
import com.example.demo.data.models.Item;
import com.example.demo.data.payloads.CartItem;
import com.example.demo.data.repository.CustomerRepository;
import com.example.demo.data.repository.ItemRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class CartServiceImpl implements CartService{
    
    @Autowired
    CustomerRepository customerRepository;

    @Autowired 
    ItemRepository itemRepository;

    private CartItem attachPrice(CartItem item) {
        Optional<Item> itemMaybe = itemRepository.findItemByNameAndVendor(item.getVendor(), item.getItemName());
        if(itemMaybe.isEmpty())
            throw new ResourceNotFoundException(item.getVendor());
        
        item.setPrice(itemMaybe.get().getPrice());
        return item;
    }

    @Override 
    public List<CartItem> getCart(String name) {
        Optional<Customer> customerMaybe = customerRepository.findByName(name);
        if(customerMaybe.isEmpty())
            throw new ResourceNotFoundException(name);

        String cart = customerMaybe.get().getCart();
        if(cart.equals(""))
            return new ArrayList<CartItem>();
        else
            return Arrays.stream(cart.split(";")).map(entry -> new CartItem(entry)).toList();
    }

    @Override
    public void setCart(String name, List<CartItem> cart) {
        Optional<Customer> customerMaybe = customerRepository.findByName(name);
        if(customerMaybe.isEmpty())
            throw new ResourceNotFoundException(name);

        String cartString = cart.stream()
                            .filter(cartItem -> cartItem.getAmount()!=0) // if zero, then deletion from cart
                            .map(item -> attachPrice(item)) // fetch price from server records
                            .map(item -> item.toString())
                            .collect(Collectors.joining(";"));
        Customer customer = customerMaybe.get();
        customer.setCart(cartString);
        customerRepository.save(customer);
    }
}
