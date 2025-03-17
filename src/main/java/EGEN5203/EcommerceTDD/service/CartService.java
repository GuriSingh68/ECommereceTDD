package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.dto.UpdateCartDto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Cart;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.CartRepo;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;

    @Transactional
    public String addItemsToCart(Long userid, AddToCartDto addToCartDto) {
        // Basic validations
        if (userid == null || addToCartDto == null) {
            throw new IllegalArgumentException("Invalid Parameters");
        }

        // Find the product
        Product product = productRepo.findByProductName(addToCartDto.getProductName());
        if (product == null) {
            return "Product not found!";
        }

        // Check stock availability
        if (product.getQuantity() < addToCartDto.getQuantity()) {
            return "Insufficient stock available!";
        }

        // Find user
        Users user = userRepo.findById(userid)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userid));
         //Find Product
        
        Cart cartItem = new Cart();
        cartItem.setProduct(product);
        cartItem.setUser(user);
        cartItem.setProductName(product.getProductName());
        cartItem.setQuantity(addToCartDto.getQuantity());
        cartItem.setPrice(addToCartDto.getQuantity() * product.getPrice());

        // Save the new cart item
        cartRepo.save(cartItem);

        // Update product inventory
        product.setQuantity(product.getQuantity() - addToCartDto.getQuantity());
        productRepo.save(product);

        return "Item added to cart successfully!";
    }
    @Transactional(readOnly = true)
    public Cart viewUserCart(Long cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }

        return cartRepo.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));
    }
    @Transactional(readOnly = true)
    public Cart updateQuantity(Long cartId, UpdateCartDto updateCartDto){
        if (cartId==null  || updateCartDto.getQuantity()==null){
            throw new IllegalArgumentException("Fields cannot be null");
        }
            Cart cart = cartRepo.findById(cartId)
                    .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));

            // Update quantity
            cart.setQuantity(updateCartDto.getQuantity());

            // Save updated cart
            return cartRepo.save(cart);
        }
@Transactional(readOnly = true)
    public String deleteCart(Long cartId,Long userId){
        if (cartId==null || userId==null){
            throw new IllegalArgumentException("Cart id cannot be null");
        }
        cartRepo.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
       Users user= userRepo.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
       if(Roles.ADMIN.equals(user.getRole())){
           cartRepo.deleteById(cartId);
           return "Cart deleted successfully";
       }
       return "Only admin can delete cart";
}
}