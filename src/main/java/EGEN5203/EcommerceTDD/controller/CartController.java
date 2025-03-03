package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/cart/{userid}")
    public String addItemsToCart(@PathVariable Long userid, @RequestBody AddToCartDto addToCartDto){
            return cartService.addItemsToCart(userid,addToCartDto);
    }

}
