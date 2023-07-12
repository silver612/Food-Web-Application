package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.data.enums.Role;

@Component
public interface NotifService {
    public String getOneSignalId(String name, Role role);
    public void setOneSignalId(String name, Role role, String newId);
    public void sendNotifToIds(List<String> ids, String message);
}
