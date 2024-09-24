package com.example.practice5_6_7.services;

import com.example.practice5_6_7.entities.Cart;
import com.example.practice5_6_7.entities.CartItem;
import com.example.practice5_6_7.entities.Client;
import com.example.practice5_6_7.entities.Product;
import com.example.practice5_6_7.exception.ResourceNotFoundException;
import com.example.practice5_6_7.jpa.CartRepository;
import com.example.practice5_6_7.jpa.ClientRepository;
import com.example.practice5_6_7.jpa.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ClientRepository clientRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    public Cart addToCart(Long clientId, Long productId, int quantity) {
        // Логика для добавления товара в корзину с проверкой наличия товара
        Client client = clientRepository.findById(clientId).get();
        Cart cart = new Cart();
        cart = cartRepository.findByClient(client).orElse(new Cart(null, client, new ArrayList<>()));

        Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Product out of stock");
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cart.getCartItems().add(cartItem);

        product.setStock(product.getStock() - quantity);
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        cart.getCartItems().remove(cartItem);  // Удаляем объект из списка
        cartRepository.save(cart);
        return cart;
    }


    public Cart updateCartItemQuantity(Long cartId, Long productId, int quantity) {
        // Логика для изменения количества товара
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cart.getCartItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        if (cartItem.getProduct().getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cart;
    }

    public Cart viewCart(Long clientId) {
        Client client = clientRepository.findById(clientId).get();
        return cartRepository.findByClient(client)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    public void checkout(Long clientId) {
        Client client = clientRepository.findById(clientId).get();
        // Логика для оформления заказа и очистки корзины
        Cart cart = cartRepository.findByClient(client)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock to complete the order");
            }
            product.setStock(product.getStock() - item.getQuantity());
        }

        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
