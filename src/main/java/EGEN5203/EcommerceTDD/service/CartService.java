// CartService.java
package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.dto.UpdateCartDto;
import EGEN5203.EcommerceTDD.model.*;
import EGEN5203.EcommerceTDD.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    public Cart createCart(AddToCartDto createCartDto) {
        Users customer = userRepo.findById(createCartDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Cart cart = new Cart();
        cart.setCustomer(customer);

        List<CartItem> items = createCartDto.getProducts().stream()
                .map(dto -> {
                    Product product = productRepo.findById(dto.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(dto.getQuantity());
                    return item;
                })
                .collect(Collectors.toList());

        cart.setItems(items);
        return cartRepo.save(cart);
    }

    @Transactional(readOnly = true)
    public List<Cart> findAll() {
        return cartRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Cart findOne(Long id) {
        return cartRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }

    @Transactional
    public Cart updateCart(Long id, UpdateCartDto updateCartDto) {
        Cart cart = cartRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Clear existing items
        cart.setItems(new ArrayList<>());
        cartItemRepo.deleteByCartId(id);

        // Add new items
        if (updateCartDto.getProducts() != null) {
            List<CartItem> items = updateCartDto.getProducts().stream()
                    .map(dto -> {
                        Product product = productRepo.findById(dto.getProductId())
                                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

                        CartItem item = new CartItem();
                        item.setCart(cart);
                        item.setProduct(product);
                        item.setQuantity(dto.getQuantity());
                        return item;
                    })
                    .collect(Collectors.toList());

            cart.setItems(items);
        }

        // Update customer if provided
        if (updateCartDto.getCustomerId() != null) {
            Users customer = userRepo.findById(updateCartDto.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            cart.setCustomer(customer);
        }

        return cartRepo.save(cart);
    }

    @Transactional
    public void deleteCart(Long id) {
        cartRepo.deleteById(id);
    }
}