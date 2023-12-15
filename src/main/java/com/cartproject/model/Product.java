package com.cartproject.model;

public class Product {
	private final int id;
    private final String description;
    private int amount;
    private final double price;

    public Product(int id, String description, int amount, double price) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.price = price;
        
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
	public double getPrice() {
		return price;
	}
    

}