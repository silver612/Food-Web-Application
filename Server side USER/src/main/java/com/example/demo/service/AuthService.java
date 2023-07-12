package com.example.demo.service;

import org.springframework.stereotype.Component;

import com.example.demo.data.enums.Role;
import com.example.demo.data.payloads.UserAuth;

@Component
public interface AuthService{

    public String checkValidityOfPassword(UserAuth userAuth);
    public boolean checkSessionId(String username, String sessionId, Role role);
    public String createUser(UserAuth userAuth);
    public void deleteUser(String username, Role role);
    public String updateUsername(String username, String newUsername, Role role);
    public boolean updatePassword(String username, UserAuth userAuth, Role role);

}
