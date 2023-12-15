package com.cartproject.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cartproject.model.Cart;
import com.cartproject.model.Product;

@Service
public class CartService {

    private final List<Cart> carts = new ArrayList<>();
    private final AtomicInteger cartIdGenerator = new AtomicInteger(1);
    
    // Method to create Initial Products
    public List<Product> getInitialProducts() {

        List<Product> products = new ArrayList<>();

        Product product = new Product(1, "Product 1", 20, 20.55);
        products.add(product);

        product = new Product(2, "Product 2", 15, 30.95);
        products.add(product);

        product = new Product(3, "Product 3", 10, 25.99);
        products.add(product);

        return products;
    }
    // Method to check if a cart exists
    public boolean cartExists(int cartId) {
        for (Cart cart : carts) {
            if (cart.getId() == cartId) {
                return true;
            }
        }
        return false;
    }

    // Method to create a new cart
    public Cart findOrCreateCart() {
        int newCartId = cartIdGenerator.getAndIncrement();
        return findOrCreateCart(newCartId);
    }

    // Method to find or create a cart with a specific cartId.
    public Cart findOrCreateCart(int cartId) {

        for (Cart cart : carts) {
            if (cart.getId() == cartId) {
                return cart;
            }
        }

        Cart newCart = new Cart(cartId);
        carts.add(newCart);
        return newCart;
    }
    // Method to add a product from the cart
    public void addProductToCart(int cartId, Product product) {
        Cart cart = findOrCreateCart(cartId);
        Optional<Product> existingProduct = cart.getProducts().stream()
                .filter(p -> p.getId() == product.getId())
                .findFirst();

        if (existingProduct.isPresent()) {
            Product foundProduct = existingProduct.get();
            foundProduct.setAmount(product.getAmount());
        }
        else {
        	cart.addProduct(product);
        }
        
    }

    // Method to remove a product from the cart
    public void removeProductFromCart(int cartId, Product product) {
    	Cart cart = findOrCreateCart(cartId);
        Optional<Product> existingProduct = cart.getProducts().stream()
                .filter(p -> p.getId() == product.getId())
                .findFirst();

        if (existingProduct.isPresent()) {
        	Product foundProduct = existingProduct.get();
        	if(product.getAmount() > 0) {
        		foundProduct.setAmount(product.getAmount());
        	}
        	else if(product.getAmount() == 0) {
        		cart.removeProduct(foundProduct);
        	}
        }
    }

    // Method to remove a expired cart from the cart
    @Scheduled(fixedRate = 600000) // 600000 milliseconds = 10 minutes
    public void cleanExpiredCartsScheduled() {
        Iterator<Cart> iterator = carts.iterator();
        while (iterator.hasNext()) {
            Cart cart = iterator.next();
            if (cart.isExpired()) {
                iterator.remove();
            }
        }
    }
    
    // Method to remove a cart from the cart List
    public void deleteCart(int cartId) {
        Iterator<Cart> iterator = carts.iterator();
        while (iterator.hasNext()) {
            Cart cart = iterator.next();
            if (cart.getId() == cartId) {
                iterator.remove();
                break;
            }
        }
    }

}
