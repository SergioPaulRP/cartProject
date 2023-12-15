package com.cartproject.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cartproject.model.Cart;
import com.cartproject.model.Product;
import com.cartproject.service.CartService;

@Controller
@RequestMapping("/")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String index() {
        Cart cart = cartService.findOrCreateCart();

        return "redirect:/carts/" + cart.getId();
    }

    @GetMapping("/carts/{cartId}")
    public String showCart(@PathVariable int cartId, Model model) {
        Cart cart = cartService.findOrCreateCart(cartId);
        List<Product> initialProducts = cartService.getInitialProducts();

        model.addAttribute("products", initialProducts);
        model.addAttribute("cart", cart);

        return "cart";

    }

    @PostMapping("/carts/{cartId}/addProduct")
    @ResponseBody
    public void addProductToCart(@PathVariable int cartId, @RequestBody Product product) {
        cartService.addProductToCart(cartId, product);

    }

    @DeleteMapping("/carts/{cartId}/removeProduct")
    @ResponseBody
    public void removeProductFromCart(@PathVariable int cartId, @RequestBody Product product) {
        cartService.removeProductFromCart(cartId, product);
    }

    @GetMapping("/carts/delete/{cartId}")
    public String deleteCart(@PathVariable int cartId, Model model) {
        cartService.deleteCart(cartId);

        Cart newCart = cartService.findOrCreateCart();
        int newCartId = newCart.getId();

        return "redirect:/carts/" + newCartId;
    }

}