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
    @Autowired
    private CartService cartService;

    @PostMapping
    public Cart create(@RequestBody AddToCartDto createCartDto) {
        return cartService.createCart(createCartDto);
    }

    @GetMapping
    public List<Cart> findAll() {
        return cartService.findAll();
    }

    @GetMapping("/{id}")
    public Cart findOne(@PathVariable Long id) {
        return cartService.findOne(id);
    }

    @PatchMapping("/{id}")
    public Cart update(@PathVariable Long id, @RequestBody UpdateCartDto updateCartDto) {
        return cartService.updateCart(id, updateCartDto);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        cartService.deleteCart(id);
    }
}