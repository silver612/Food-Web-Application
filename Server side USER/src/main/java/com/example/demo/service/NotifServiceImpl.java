package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.data.enums.Role;
import com.example.demo.data.models.Customer;
import com.example.demo.data.models.Vendor;
import com.example.demo.data.repository.CustomerRepository;
import com.example.demo.data.repository.VendorRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class NotifServiceImpl implements NotifService {
    
    @Value("${onesignal.notif.uri}")
    private String notifUri;

    @Value("${onesignal.notif.players}")
    private String playersUrl;

    @Value("${onesignal.notif.apikey}")
    private String apiKey;

    @Value("${onesignal.notif.appid}")
    private String appId;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    VendorRepository vendorRepository;

    private void deleteOnesignalIds(String id){
        
        String deletePlayersUri = playersUrl + "/" + id  + "?app_id=" + appId;
        
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + apiKey);

        HttpEntity<?> bodyEntity = new HttpEntity<>(httpHeaders);
        
        HttpEntity<?> response = restTemplate.exchange(
            deletePlayersUri, 
            HttpMethod.DELETE, 
            bodyEntity, 
            String.class, 
            httpHeaders);

        System.out.println("Onesignal deleting players response: " + response);
    }

    @Override
    public String getOneSignalId(String name, Role role){
        
        if(role.equals(Role.USER))
        {
            Optional<Customer> customerMaybe = customerRepository.findByName(name);
            if(customerMaybe.isEmpty())
                throw new ResourceNotFoundException(name);

            return customerMaybe.get().getOneSignalId();
        }
        else if(role.equals(Role.VENDOR))
        {
            Optional<Vendor> vendorMaybe = vendorRepository.findByName(name);
            if(vendorMaybe.isEmpty())
                throw new ResourceNotFoundException(name);

            return vendorMaybe.get().getOneSignalId();
        }
        return null;
    }

    @Override
    public void setOneSignalId(String name, Role role, String newId){
        String oldId = null;
        if(role.equals(Role.USER))
        {
            Optional<Customer> customerMaybe = customerRepository.findByName(name);
            if(customerMaybe.isEmpty())
                throw new ResourceNotFoundException(name);

            Customer customer = customerMaybe.get();
            oldId = customer.getOneSignalId();
            customer.setOneSignalId(newId);
            customerRepository.save(customer);
        }
        else if(role.equals(Role.VENDOR))
        {
            Optional<Vendor> vendorMaybe = vendorRepository.findByName(name);
            if(vendorMaybe.isEmpty())
                throw new ResourceNotFoundException(name);

            Vendor vendor = vendorMaybe.get();
            oldId = vendor.getOneSignalId();
            vendor.setOneSignalId(newId);
            vendorRepository.save(vendor);
        }
        // if(oldId==null || oldId.equals(newId))
        //     deleteOnesignalIds(oldId);
    }

    @Override
    public void sendNotifToIds(List<String> ids, String message){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + apiKey);
        httpHeaders.add("accept", "application/json");
        httpHeaders.add("content-type", "application/json");

        String body = "{" +  
                "    \"app_id\": \"" + appId + "\"," + 
                "    \"include_player_ids\": [" + ids.stream().map(id -> "\"" + id + "\"").collect(Collectors.joining(",")) + "]," + 
                "    \"contents\": {\"en\": \"Open or Refresh orders page on website to view.\"}," + 
                "    \"headings\": {\"en\": \"" + message + "\"}" + 
                "}";
        HttpEntity<?> bodyEntity = new HttpEntity<>(body, httpHeaders);

        HttpEntity<?> response = restTemplate.exchange(
            notifUri, 
            HttpMethod.POST, 
            bodyEntity, 
            String.class, 
            httpHeaders);
        
        System.out.println("Onesignal notif response: " + response);
    }
}
