package com.example.practice5_6_7.controllers;

import com.example.practice5_6_7.entities.Cart;
import com.example.practice5_6_7.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{clientId}/add")
    public ResponseEntity<Cart> addToCart(@PathVariable Long clientId, @RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(clientId, productId, quantity));
    }

    @DeleteMapping("/{cartId}/remove")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long cartId, @RequestParam Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(cartId, productId));
    }

    @PutMapping("/{cartId}/update")
    public ResponseEntity<Cart> updateCartItemQuantity(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(cartId, productId, quantity));
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Cart> viewCart(@PathVariable Long clientId) {
        return ResponseEntity.ok(cartService.viewCart(clientId));
    }

    @PostMapping("/{clientId}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable Long clientId) {
        cartService.checkout(clientId);
        return ResponseEntity.ok().build();
    }
}
