package com.example.demo.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.enums.OrderStatus;
import com.example.demo.data.models.Customer;
import com.example.demo.data.models.Item;
import com.example.demo.data.models.Order;
import com.example.demo.data.models.OrderSegment;
import com.example.demo.data.models.Vendor;
import com.example.demo.data.payloads.CartItem;
import com.example.demo.data.payloads.OrderDetails;
import com.example.demo.data.payloads.OrderShort;
import com.example.demo.data.repository.CustomerRepository;
import com.example.demo.data.repository.ItemRepository;
import com.example.demo.data.repository.OrderRepository;
import com.example.demo.data.repository.VendorRepository;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedAccessException;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    CartService cartServiceImpl;

    @Autowired
    NotifService notifServiceImpl;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ItemRepository itemRepository;

    @Override
    public void createOrder(String name){
        Optional<Customer> customerMaybe = customerRepository.findByName(name);
        if(customerMaybe.isEmpty())
            throw new ResourceNotFoundException(name);
        
        Order order = new Order();
        order.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC).toString());
        Customer customer = customerMaybe.get();
        order.setCustomer(customer);
        
        Map<String, List<CartItem> > cartItemsByVendor = cartServiceImpl.getCart(name).stream()
                            .collect(Collectors.groupingBy(obj->obj.getVendor()));
        Map<String, Map<Item, Integer> > cartByVendor = new HashMap<>();
        cartItemsByVendor.forEach((k, v) ->                    
                                    {
                                        Map<Item, Integer> cart = new HashMap<>();
                                        v.forEach(cartItem -> {
                                            Optional<Item> itemMaybe = itemRepository.findItemByNameAndVendor(cartItem.getVendor(), cartItem.getItemName());
                                            if(itemMaybe.isEmpty())
                                                throw new ResourceNotFoundException(cartItem.getVendor() + "and Item " + cartItem.getItemName());
                                            cart.put(itemMaybe.get(), cartItem.getAmount());
                                        });
                                        if(cart.size()!=0)
                                            cartByVendor.put(k, cart); 
                                    });
        
        
        List<String> vendorNotifIds = new ArrayList<>();
        
        cartByVendor.forEach((v,o) -> {
            Optional<Vendor> vendorMaybe = vendorRepository.findByName(v);
            if(vendorMaybe.isEmpty())
                throw new ResourceNotFoundException(v);
            OrderSegment orderSegment = new OrderSegment(vendorMaybe.get(), o);
            order.addOrderSegment(orderSegment); 
            
            if(vendorMaybe.get().getOneSignalId()!=null) 
                vendorNotifIds.add(vendorMaybe.get().getOneSignalId());
        });

        orderRepository.save(order);

        notifServiceImpl.sendNotifToIds(vendorNotifIds, "New order made. Order id: " + order.getId());

        customer.setCart("");
        customerRepository.save(customer);
    }

    @Override
    public List<OrderShort> getOrdersForCustomer(String name){
        Optional<Customer> customerMaybe = customerRepository.findByName(name);
        if(customerMaybe.isEmpty())
            throw new ResourceNotFoundException(name);
        Customer customer = customerMaybe.get();
        return customer.getOrders().stream().map(order->new OrderShort(order.getId(), order.getCreatedAt())).toList();
    }

    @Override
    public OrderDetails getOrderForCustomer(String name, Integer id){
        Optional<Order> orderMaybe = orderRepository.findById(id);
        if(orderMaybe.isEmpty())
            throw new ResourceNotFoundException("Order id: " + id);
        if(!orderMaybe.get().getCustomer().getName().equals(name))
            throw new UnauthorizedAccessException();
        
        OrderDetails orderDetails = new OrderDetails();
        Order order = orderMaybe.get();
        orderDetails.setId(order.getId());
        order.getOrderSegments().forEach(ordersegment -> {
            
            orderDetails.getVendors().add(ordersegment.getVendor().getName());
            orderDetails.getStatuses().add(ordersegment.getStatus());
            
            List<String> itemList = new ArrayList<>();
            List<Integer> qtyLists = new ArrayList<>();
            
            ordersegment.getItems().forEach((item, qty) ->{
                itemList.add(item.getName());
                qtyLists.add(qty);
            });
            float price = (float) ordersegment.getItems().entrySet()
                                    .stream()
                                    .mapToDouble(entry->entry.getKey().getPrice()*entry.getValue())
                                    .sum();
            orderDetails.getPrices().add(price);
            
            orderDetails.getItemLists().add(itemList);
            orderDetails.getQtyLists().add(qtyLists);
        }); 
        return orderDetails;
    }

    @Override
    public List<OrderShort> getOrdersForVendor(String name){
        Optional<Vendor> vendorMaybe = vendorRepository.findByName(name);
        if(vendorMaybe.isEmpty())
            throw new ResourceNotFoundException(name);
        Vendor vendor = vendorMaybe.get();
        return vendor.getOrderSegments().stream().map(orderSegment -> orderSegment.getOrder()).map(order->new OrderShort(order.getId(), order.getCreatedAt())).toList();
    }

    @Override
    public OrderDetails getOrderSegmentForVendor(String name, Integer id){
        Optional<Order> orderMaybe = orderRepository.findById(id);
        if(orderMaybe.isEmpty())
            throw new ResourceNotFoundException("Order id: " + id);
        
        OrderDetails orderDetails = new OrderDetails();
        Order order = orderMaybe.get();
        orderDetails.setId(order.getId());
        
        OrderSegment orderSegment = order.getOrderSegments().stream()
            .filter(ordersegment -> ordersegment.getVendor().getName().equals(name))
            .findFirst().orElseThrow(UnauthorizedAccessException::new); 
        
        orderDetails.getVendors().add(orderSegment.getVendor().getName());
        orderDetails.getStatuses().add(orderSegment.getStatus());
        
        List<String> itemList = new ArrayList<>();
        List<Integer> qtyLists = new ArrayList<>();
        
        orderSegment.getItems().forEach((item, qty) ->{
            itemList.add(item.getName());
            qtyLists.add(qty);
        });

        float price = (float) orderSegment.getItems().entrySet()
                                    .stream()
                                    .mapToDouble(entry->entry.getKey().getPrice()*entry.getValue())
                                    .sum();
        orderDetails.getPrices().add(price);
        
        orderDetails.getItemLists().add(itemList);
        orderDetails.getQtyLists().add(qtyLists);
         
        return orderDetails;
    }

    @Override
    public boolean setOrderSegmentStatus(String name, Integer id, OrderStatus newStatus){
        Optional<Order> orderMaybe = orderRepository.findById(id);
        if(orderMaybe.isEmpty())
            throw new ResourceNotFoundException("Order id: " + id);

        Order order = orderMaybe.get();
        OrderSegment orderSegment = order.getOrderSegments().stream()
            .filter(ordersegment -> ordersegment.getVendor().getName().equals(name))
            .findFirst().orElseThrow(UnauthorizedAccessException::new);
        
        Customer customer = customerRepository.findByName(order.getCustomer().getName())
                            .orElseThrow(() -> new ResourceNotFoundException("Customer for order id: " + id));

        OrderStatus currentStatus = orderSegment.getStatus();
        
        if( 
            (currentStatus.equals(OrderStatus.ASKING)
            && (newStatus.equals(OrderStatus.INPROGRESS) || newStatus.equals(OrderStatus.REJECTED)))
          ||
            (currentStatus.equals(OrderStatus.INPROGRESS)
            && newStatus.equals(OrderStatus.TRANSPORTING))
          ||
            (currentStatus.equals(OrderStatus.TRANSPORTING)
            && newStatus.equals(OrderStatus.DELIVERED)) 
          || 
            currentStatus.equals(newStatus)
          )
        {
            orderSegment.setStatus(newStatus);
            orderRepository.save(order);
            if(!currentStatus.equals(newStatus))
                notifServiceImpl.sendNotifToIds(Arrays.asList(customer.getOneSignalId()), "Status update for order id: " + id);
            return true;
        }
        else
            return false;   
    }
}
