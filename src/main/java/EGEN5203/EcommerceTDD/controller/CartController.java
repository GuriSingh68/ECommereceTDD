// CartController.java
package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.dto.UpdateCartDto;
import EGEN5203.EcommerceTDD.model.Cart;
import EGEN5203.EcommerceTDD.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    /***
     * Cart service containing business logic
     */
    @Autowired
    private CartService cartService;

    /**
     *  Create a cart by the user
     * @param createCartDto
     * @return Cart created details
     */
    @PostMapping
    public Cart create(@RequestBody AddToCartDto createCartDto) {
        return cartService.createCart(createCartDto);
    }

    /***
     *  Lists all the carts
     * @return all the carts
     */
    @GetMapping
    public List<Cart> findAll() {
        return cartService.findAll();
    }

    /**
     * Lists cart by ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Cart findOne(@PathVariable Long id) {
        return cartService.findOne(id);
    }

    /**
     * Update Cart items
     * @param id
     * @param updateCartDto
     * @return
     */

    @PatchMapping("/{id}")
    public Cart update(@PathVariable Long id, @RequestBody UpdateCartDto updateCartDto) {
        return cartService.updateCart(id, updateCartDto);
    }

    /**
     * Delete the cart
     * @param id
     */
    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        cartService.deleteCart(id);
    }
}