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
    /**
     * Cart repository to connect with database
     */
    @Autowired
    private CartRepo cartRepo;
    /**
     * Cart item repository to connect with database
     */
    @Autowired
    private CartItemRepo cartItemRepo;
    /**
     * Product repository to connect with database
     */
    @Autowired
    private ProductRepo productRepo;
    /**
     * User repository to connect with database
     */
    @Autowired
    private UserRepo userRepo;

    /**
     * Logic for creating a cart
     * @param createCartDto
     * @return
     */
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

    /**
     * List all carts
     * @return
     */
    @Transactional(readOnly = true)
    public List<Cart> findAll() {
        return cartRepo.findAll();
    }

    /**
     * Find cart by ID
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Cart findOne(Long id) {
        return cartRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }

    /**
     * Update cart items and quantity
     * @param id
     * @param updateCartDto
     * @return
     */
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

    /**
     * Delete a cart
     * @param id
     */
    @Transactional
    public void deleteCart(Long id) {
        cartRepo.deleteById(id);
    }
}