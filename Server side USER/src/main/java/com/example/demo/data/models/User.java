package com.example.demo.data.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String oneSignalId;

    private String name;

    private String password;

    private Long sessionId;

    public Integer getId() {
        return id;
    }

    public String getOneSignalId() {
        return oneSignalId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setOneSignalId(String newId) {
        this.oneSignalId = newId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
}
