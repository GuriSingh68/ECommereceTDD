package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.model.Cart;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.repo.CartRepo;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ProductRepo productRepo;
    public String addItemsToCart(Long userid, AddToCartDto addToCartDto) {
        Product product=productRepo.findByProductName(addToCartDto.getProductName());
        if (product == null) {
            return "Product not found!";
        }
        if (product.getQuantity() < addToCartDto.getQuantity()) {
            return "Insufficient stock available!";
        }
        if(product.getQuantity()>addToCartDto.getQuantity()){
            Cart cart = new Cart();
            cart.setUserId(userid);
            cart.setProductName(product.getProductName());
            cart.setQuantity(addToCartDto.getQuantity());
            cart.setPrice(product.getPrice() * addToCartDto.getQuantity()); // Total price

            // Save to the cart table
            cartRepo.save(cart);

            // Update product quantity in the product table
            product.setQuantity(product.getQuantity() - addToCartDto.getQuantity());
            productRepo.save(product);

            return "Item added to cart successfully!";
        }
        else{
            throw new IllegalArgumentException("Not able to add product");
        }
    }
}
