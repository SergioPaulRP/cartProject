package com.cartproject.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Cart {
	private final int id;
    private final List<Product> products = new ArrayList<>();
    private Instant creationDate;
    private static final int TTL_SECONDS = 600;

	
	public Cart(int id) {
        this.id = id;
        this.creationDate = Instant.now();
    }

    public int getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public boolean isExpired() {
        Instant currentTime = Instant.now();
        Instant expirationTime = creationDate.plusSeconds(TTL_SECONDS);
        return currentTime.isAfter(expirationTime);
    }
}
