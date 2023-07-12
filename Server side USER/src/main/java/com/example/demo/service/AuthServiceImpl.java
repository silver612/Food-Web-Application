package com.example.demo.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;

import com.example.demo.data.enums.Role;
import com.example.demo.data.models.Customer;
import com.example.demo.data.models.Vendor;
import com.example.demo.data.payloads.UserAuth;
import com.example.demo.data.repository.CustomerRepository;
import com.example.demo.data.repository.VendorRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class AuthServiceImpl implements AuthService{
    
    @Autowired
    CustomerRepository userRepository;

    @Autowired
    VendorRepository vendorRepository;

    private String encryptPassword(String password)
    {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger bigInteger = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(bigInteger.toString(16));
            while(hexString.length() < 32)
                hexString.insert(0, '0');
            return hexString.toString();
        }
        catch(NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm");
            return null;
        }
    }

    @Override
    public String checkValidityOfPassword(UserAuth userAuth)
    {
        Optional<?> userMaybe;
        if(userAuth.getRole()==Role.USER)
            userMaybe = userRepository.findByName(userAuth.getUsername());
        else if(userAuth.getRole()==Role.VENDOR)
            userMaybe = vendorRepository.findByName(userAuth.getUsername());
        else
            throw new ConversionFailedException(null, null, "", new Throwable()); //Reaches here only if role is null
        
        if(userMaybe.isEmpty())
            throw new ResourceNotFoundException(userAuth.getUsername());
        
        if(userAuth.getRole()==Role.USER)
        {   
            Customer user = (Customer) userMaybe.get();
            if(encryptPassword(userAuth.getPassword()).equals(user.getPassword()))
            {
                SecureRandom secureRandom = new SecureRandom();
                secureRandom.setSeed(System.currentTimeMillis());
                Long sessionId = secureRandom.nextLong();
                user.setSessionId(sessionId);
                userRepository.save(user);

                return sessionId.toString();
            }
            else
                return "Incorrect Password";
        }
        else if(userAuth.getRole()==Role.VENDOR)
        {
            Vendor vendor = (Vendor) userMaybe.get();
            if(encryptPassword(userAuth.getPassword()).equals(vendor.getPassword()))
            {
                SecureRandom secureRandom = new SecureRandom();
                secureRandom.setSeed(System.currentTimeMillis());
                Long sessionId = secureRandom.nextLong();
                vendor.setSessionId(sessionId);
                vendorRepository.save(vendor);

                return sessionId.toString();
            }
            else
                return "Incorrect Password";
        }
        else 
            return ""; //will not reach here
    }

    @Override 
    public boolean checkSessionId(String username, String sessionId, Role role)
    {
        Optional<?> userMaybe;
        if(role==Role.USER)
            userMaybe = userRepository.findByName(username);
        else if(role==Role.VENDOR)
            userMaybe = vendorRepository.findByName(username);
        else
            throw new ConversionFailedException(null, null, "", new Throwable());
        
        if(userMaybe.isEmpty())
            throw new ResourceNotFoundException(username);
        if((role==Role.USER && ((Customer) userMaybe.get()).getSessionId().toString().equals(sessionId)) 
        || (role==Role.VENDOR && ((Vendor) userMaybe.get()).getSessionId().toString().equals(sessionId)))
            return true;
        else
            return false;
    }

    @Override
    public String createUser(UserAuth userAuth)
    {
        if((userAuth.getRole()==Role.USER && userRepository.findByName(userAuth.getUsername()).isPresent()) 
        || (userAuth.getRole()==Role.VENDOR && vendorRepository.findByName(userAuth.getUsername()).isPresent()))
            return "ALREADY_EXISTS";

        Long sessionId;

        if(userAuth.getRole()==Role.USER)
        {   
            Customer user = new Customer();
            user.setName(userAuth.getUsername());
            user.setPassword(encryptPassword(userAuth.getPassword()));

            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(System.currentTimeMillis());
            sessionId = secureRandom.nextLong();
            user.setSessionId(sessionId);
            userRepository.save(user);
        }
        else if(userAuth.getRole()==Role.VENDOR)
        {
            Vendor vendor = new Vendor();
            vendor.setName(userAuth.getUsername());
            vendor.setPassword(encryptPassword(userAuth.getPassword()));

            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(System.currentTimeMillis());
            sessionId = secureRandom.nextLong();
            vendor.setSessionId(sessionId);
            vendorRepository.save(vendor);
        }
        else 
            throw new ConversionFailedException(null, null, "", new Throwable());

        return sessionId.toString();
    }

    @Override
    public void deleteUser(String username, Role role)
    {
        Optional<?> userMaybe;
        if(role==Role.USER)
            userMaybe = userRepository.findByName(username);
        else if(role==Role.VENDOR)
            userMaybe = vendorRepository.findByName(username);
        else
            throw new ConversionFailedException(null, null, "", new Throwable());
        
        if(userMaybe.isEmpty())
            throw new ResourceNotFoundException(username);

        if(role==Role.USER)
            userRepository.deleteById(((Customer) userMaybe.get()).getId());
        else if(role==Role.VENDOR)
            vendorRepository.deleteById(((Vendor) userMaybe.get()).getId());
    }

    @Override
    public String updateUsername(String username, String newUsername, Role role)
    {
        Optional<?> userMaybe;
        if(role==Role.USER)
            userMaybe = userRepository.findByName(username);
        else if(role==Role.VENDOR)
            userMaybe = vendorRepository.findByName(username);
        else 
            throw new ConversionFailedException(null, null, "", new Throwable());

        if(userMaybe.isEmpty())
            throw new ResourceNotFoundException(username);

        if((role==Role.USER && userRepository.findByName(newUsername).isPresent()) 
        || (role==Role.VENDOR && vendorRepository.findByName(newUsername).isPresent()))
            return "ALREADY_EXISTS";

        if(role==Role.USER)
        {
            Customer user = (Customer) userMaybe.get();
            user.setName(newUsername);
            userRepository.save(user);
        }
        else if(role==Role.VENDOR)
        {
            Vendor vendor = (Vendor) userMaybe.get();
            vendor.setName(newUsername);
            vendorRepository.save(vendor);
        }
        return "OK";
    }

    @Override
    public boolean updatePassword(String username, UserAuth userAuth, Role role)
    {
        Optional<?> userMaybe;
        if(role==Role.USER)
            userMaybe = userRepository.findByName(username);
        else if(role==Role.VENDOR)
            userMaybe = vendorRepository.findByName(username);
        else
            throw new ConversionFailedException(null, null, "", new Throwable());
        if(userMaybe.isEmpty())
            throw new ResourceNotFoundException(username);
        if(role==Role.USER) 
        {
            Customer user = (Customer) userMaybe.get();
            if(encryptPassword(userAuth.getPassword()).equals(user.getPassword())){
                user.setPassword(encryptPassword(userAuth.getNewPassword()));
                userRepository.save(user);
                return true;
            }
            else
                return false;
        }
        else if(role==Role.VENDOR)
        {
            Vendor user = (Vendor) userMaybe.get();
            if(encryptPassword(userAuth.getPassword()).equals(user.getPassword())){
                user.setPassword(encryptPassword(userAuth.getNewPassword()));
                vendorRepository.save(user);
                return true;
            }
            else
                return false;
        }
        else 
            return false; // will not reach here
    }
}
