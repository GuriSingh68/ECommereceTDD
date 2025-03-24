package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.dto.UpdateCartDto;
import EGEN5203.EcommerceTDD.dto.UpdateCartQuantityDto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Cart;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.CartRepo;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // Find user
        Users user = userRepo.findById(userid)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userid));
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
    public Cart updateQuantity(Long cartId, UpdateCartQuantityDto updateCartQuantityDto){
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));
        Product product=productRepo.findById(cart.getProduct().getProduct_id())
                .orElseThrow(
                        () -> new IllegalArgumentException("Product not found"));

            if(product.getQuantity()< updateCartQuantityDto.getQuantity()){
                throw new IllegalArgumentException("Not enough quantity");
            }
            // Update quantity
            cart.setQuantity(updateCartQuantityDto.getQuantity());

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
@Transactional
    public Cart updateCartQuantity(Long cartId, Integer quantity) {
            if (cartId ==null || quantity ==null){
                throw new IllegalArgumentException("Cart id or Quantity cannot be null");
            }
            Cart cart=cartRepo.findById(cartId)
                    .orElseThrow(() -> new IllegalArgumentException("Cart cannot found"));
            cart.setQuantity(quantity);
            return cart;
    }

    public Cart updateCartItems(Long cartId, Long userId, UpdateCartDto updateCartDto) {
        Users user=userRepo.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        Cart cart=cartRepo.findById(cartId).orElseThrow(
                () -> new IllegalArgumentException("Cart not found")
        );
        if (user.getRole().equals(Roles.ADMIN)){

           if (updateCartDto.getQuantity() !=null){
               cart.setQuantity(updateCartDto.getQuantity());
           }
           if (updateCartDto.getProductName() !=null){
               cart.setProductName(updateCartDto.getProductName());
           }
           if (updateCartDto.getPrice()!=null){
               cart.setPrice(updateCartDto.getPrice());
           }
            return cartRepo.save(cart);
        }
        throw new IllegalArgumentException("Only admin can change these fields");
    }
}