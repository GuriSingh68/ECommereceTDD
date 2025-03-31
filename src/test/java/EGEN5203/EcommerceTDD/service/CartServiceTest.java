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
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @InjectMocks
    CartService cartService;
    @Mock
    ProductRepo productRepo;
    @Mock
    UserRepo userRepo;
    @Mock
    CartRepo cartRepo;
    private AddToCartDto addToCartDto;
    private UpdateCartDto updateCartDto;
    private UpdateCartQuantityDto updateCartQuantityDto;
    private Users users;
    private Product product;
    private String addToCartResult;
    private Cart cart;
    /**
     *Setting up product and dto before each test
     */
    @BeforeEach
    void setUp(){

        addToCartDto =new AddToCartDto();
        addToCartDto.setProductName("Xbox");
        addToCartDto.setQuantity(2);
        product= Product.builder()
                .product_id(1L)
               .productName("Xbox")
               .description("Gaming controller")
               .category("Electronics")
               .quantity(10)
               .price(120.0)
               .build();
        cart = new Cart();
        cart.setCart_id(1L);
        cart.setProduct(Product.builder().product_id(1L).build());
        cart.setProductName("Xbox");
        cart.setQuantity(2);
        cart.setPrice(240.0);
        users =new Users();
        users.setFirstName("Guri");
        users.setLastName("Singh");
        users.setEmail("abc@xyz.com");
        users.setUser_id(1L);
        updateCartQuantityDto = new UpdateCartQuantityDto();
        updateCartQuantityDto.setQuantity(3);
        updateCartDto = new UpdateCartDto();
        updateCartDto.setProductName("PS5");
        updateCartDto.setQuantity(1);
        updateCartDto.setPrice(500.0);
    }

    /**
     * Testing adding items to cart successfully
     */
    @Test
    void addItemsToCart() {
        //Arrange
        when(productRepo.findByProductName(addToCartDto.getProductName())).thenReturn(product);
        when(userRepo.findById(1L)).thenReturn(Optional.of(users));
        //Act
        addToCartResult=cartService.addItemsToCart(1L,addToCartDto);
        //Assert
        assertEquals("Item added to cart successfully!",addToCartResult);
        //Decrease in quantity depicting items has been added to cart
        assertEquals(8,product.getQuantity());
    }
    @Test
    void addItemsToCart_InvalidUserId() {
        // Arrange
        when(userRepo.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addItemsToCart(10L, addToCartDto);
        });

        // Assert the exception message
        assertEquals("User not found with ID: 10", exception.getMessage());
    }
    //Testing with invalid params
    @Test
    void addItemsToCart_InvalidParameters() {
        //Arrange
       when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(users));
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addItemsToCart(1L, null);
        });

        assertEquals("Invalid Parameters", exception.getMessage());

    }
//Testing product not found
@Test
void addItemsToCart_ProductNotFound() {
        //Arrange
    when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(users));
        addToCartDto=new AddToCartDto();
        addToCartDto.setQuantity(2);
        addToCartDto.setProductName("damn");
    String result = cartService.addItemsToCart(1L,addToCartDto);

    assertEquals("Product not found!", result);
}
    //Testing insufficient stock
    // When user add a quantity more than the stock available
    @Test
    void addItemsToCart_InsufficientStock() {
        //Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(users));
        product.setQuantity(1); // Set stock to 1
        //Act
        when(productRepo.findByProductName("Xbox")).thenReturn(product);
        String result = cartService.addItemsToCart(1L, addToCartDto);
         //Assert
        assertEquals("Insufficient stock available!", result);
    }
    //Testing successfully fetching a cart
    @Test
    void viewUserCart() {
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.viewUserCart(1L);

        assertEquals(cart, result);
    }
    //Testing finding a cart whose id does not exist
    @Test
    void viewUserCart_CartNotFound() {
        //Act
        when(cartRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.viewUserCart(1L);
        });
        //Asset

        assertEquals("Cart not found with ID: 1", exception.getMessage());
    }

    @Test
    void viewUserCart_CartIdNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.viewUserCart(null);
        });

        assertEquals("Cart ID cannot be null", exception.getMessage());
    }
//Testing updating the quantity
    @Test
    void updateQuantity() {
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.updateQuantity(1L, updateCartQuantityDto);

        assertEquals(3, result.getQuantity());
    }
    // Testing cart not found
    @Test
    void updateQuantity_CartNotFound() {
        //Act
        when(cartRepo.findById(1L)).thenReturn(Optional.empty());
    //Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateQuantity(1L, updateCartQuantityDto);
        });
        //Arrange
        assertEquals("Cart not found with ID: 1", exception.getMessage());
    }
    //Testing product not found
    @Test
    void updateQuantity_ProductNotFound(){
        //Act
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(Optional.empty());
        //Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->{
            cartService.updateQuantity(1L,updateCartQuantityDto);
        });

        assertEquals("Product not found", exception.getMessage());
    }
    //Testing trying to update quantity having insufficient quantity
    @Test
    void updateQuantity_InsufficientQuantity() {
        //Act
        product.setQuantity(2);
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
    //Arrange
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateQuantity(1L, updateCartQuantityDto);
        });
        //Assert
        assertEquals("Not enough quantity", exception.getMessage());
    }
    //Testing deleting a cart by admin
    @Test
    void deleteCart() {
        //Arrange
        users.setRole(Roles.ADMIN);
        //Act
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        when(userRepo.findById(1L)).thenReturn(Optional.of(users));

        String result = cartService.deleteCart(1L, 1L);
        //Assert
        assertEquals("Cart deleted successfully", result);

    }
    // Deleting a cart not found
    @Test
    void deleteCart_CartNotFound() {
        //Arrange + Act
        when(cartRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCart(1L, 1L);
        });
        //Assert
        assertEquals("Cart not found", exception.getMessage());
    }
    // deleting a cart for which users doesn't exist
    @Test
    void deleteCart_UserNotFound() {
        //Arrange+assert
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCart(1L, 1L);
        });
        //Assert
        assertEquals("User not found", exception.getMessage());
    }
    // when a user tries to delete a cart whose roles is not admin
    @Test
    void deleteCart_UserNotAdmin() {
        //Act + Arrange
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        when(userRepo.findById(1L)).thenReturn(Optional.of(users));

        String result = cartService.deleteCart(1L, 1L);
    //Assert
        assertEquals("Only admin can delete cart", result);
    }

    /**
     *  Testing the happy flow of updating cart by the user
     */
    @Test
    void updateCartItems() {
        //Arrange
        users.setRole(Roles.ADMIN);
        when(userRepo.findById(1L)).thenReturn(Optional.of(users));
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);
        //Act
        Cart updatedCart=cartService.updateCartItems(1L,1L,updateCartDto);
        //Assert
        assertEquals(updateCartDto.getQuantity(), updatedCart.getQuantity());
        assertEquals(updateCartDto.getProductName(), updatedCart.getProductName());
        assertEquals(updateCartDto.getPrice(), updatedCart.getPrice());
    }

    /**
     * When @user who is not admin trie to change quantity of other carts
     */
    @Test
    void userNotAdminUpdatingCartItems(){
        //Act + arrange
        users.setRole(Roles.USER);
        when(userRepo.findById(1L)).thenReturn(Optional.of(users));
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        //Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateCartItems(1L,1L,updateCartDto);
        });
        assertEquals("Only admin can change these fields", exception.getMessage());
    }
    /**
     * when user which does not exist tries to update cart
     */
    @Test
    void user_not_existing_updates_caart(){
        //Act + arrange
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
//         when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        //Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateCartItems(1L,1L,updateCartDto);
        });
        assertEquals("User not found", exception.getMessage());
    }
    /**
     * When admin tries to update cart which doen't exist
     */
    @Test
    void adminUpdatesNonExistingCart(){
        users.setRole(Roles.ADMIN);
        //Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(users));
        when(cartRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateCartItems(1L,1L,updateCartDto);
        });
        assertEquals("Cart not found", exception.getMessage());
    }
    @Test
    void updateCartQuantity(){
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
//        when(cartRepo.save(any(Cart.class))).thenReturn(cart);
        Cart updatedCart = cartService.updateCartQuantity(1L, 20);
        assertNotNull(updatedCart);
        assertEquals(20, updatedCart.getQuantity()); //Assert the result of the method.
    }
    //Testing updating cart quantity for which cart doesn't exists
    @Test
    void updateCartQuantity_cartNotFound() {
        when(cartRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> cartService.updateCartQuantity(1L, 20));
    }
    //Testing updating cart with null values
    @Test
    void updateCartQuantity_nullArguments(){
        assertThrows(IllegalArgumentException.class, ()-> cartService.updateCartQuantity(null,20));
        assertThrows(IllegalArgumentException.class, ()-> cartService.updateCartQuantity(1L,null));
    }
}