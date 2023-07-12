package com.example.demo.data.payloads;

public class CartItem {
    private String vendor, itemName;
    private Float price;
    private Integer amount;

    public CartItem() {
        vendor = "";
        itemName = "";
        amount = null; 
        price = null;
    }

    public CartItem(String cartString) {
        String[] parts = cartString.split(",");
        vendor = parts[0];
        itemName = parts[1];
        amount = Integer.parseInt(parts[2]);
        price = Float.parseFloat(parts[3]);
    }


    
    public CartItem(String vendor, String itemName, Integer amount, Float price) {
        this.vendor = vendor;
        this.itemName = itemName;
        this.amount = amount;
        this.price = price;
    }

    @Override
    public String toString(){
        return vendor + "," + itemName + "," + amount + "," + price;
    }

    public String getVendor() {
        return vendor;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getAmount() {
        return amount;
    }

    public Float getPrice() {
        return price;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
