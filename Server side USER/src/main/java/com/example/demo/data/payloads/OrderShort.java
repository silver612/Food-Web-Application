package com.example.demo.data.payloads;

public class OrderShort {
    
    private Integer id;
    private String createdAt;

    public OrderShort() {
    }
    
    public OrderShort(Integer id, String createdAt) {
        this.createdAt = createdAt;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
