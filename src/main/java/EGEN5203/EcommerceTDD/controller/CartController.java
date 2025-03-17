package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.dto.UpdateCartDto;
import EGEN5203.EcommerceTDD.model.Cart;
import EGEN5203.EcommerceTDD.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/cart/{userid}")
    public String addItemsToCart(@PathVariable Long userid, @RequestBody AddToCartDto addToCartDto){
            return cartService.addItemsToCart(userid,addToCartDto);
    }
    @GetMapping("viewCart/{cartId}")
    public Cart viewCart(@PathVariable Long cartId){
        return cartService.viewUserCart(cartId);
    }
    @PatchMapping("/updateCart/{cartId}")
    public Cart updateCart(@PathVariable long cartId,@RequestBody UpdateCartDto updateCartDto){
        return cartService.updateQuantity(cartId,updateCartDto);
    }
    @DeleteMapping("/deleteCart/{cartId}/{userId}")
    public String deleteCart(@PathVariable long cartId,@PathVariable long userId){
        return cartService.deleteCart(cartId,userId);
    }
}
