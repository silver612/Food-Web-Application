package com.example.demo.data.payloads;

public class ItemChange {
    
    private String name, newName;
    private Float newPrice;

    public String getName() {
        return name;
    }

    public String getNewName() {
        return newName;
    }

    public Float getNewPrice() {
        return newPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public void setNewPrice(Float newPrice) {
        this.newPrice = newPrice;
    }
}
