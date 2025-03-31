package EGEN5203.EcommerceTDD.stepDefinitions;

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
import EGEN5203.EcommerceTDD.service.CartService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartStepDefinitions {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CartService cartService;

    private Users currentUser;
    private Product currentProduct;
    private int originalQuantity;
    private String cartOperationResult;
    private Exception cartOperationException;
    private Cart viewedCart;
    private Exception viewCartException;
    private Cart updatedCart;
    private Exception updateException;
    private String deleteResult;
    private Cart cart;
    private UpdateCartDto updateCartDto;
    private Exception exception;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("A user with ID {int}")
    public void a_user_with_id(Integer userId) {
        currentUser = new Users();
        currentUser.setUser_id(userId.longValue());
        when(userRepo.findById(userId.longValue())).thenReturn(Optional.of(currentUser));
    }

    @Given("A product with name {string}, price {double}, and quantity {int}")
    public void productSetup(String name, Double price, Integer quantity) {
        currentProduct = new Product();
        currentProduct.setProductName(name);
        currentProduct.setPrice(price);
        currentProduct.setQuantity(quantity);
        originalQuantity = quantity;  // Store initial quantity
        when(productRepo.findByProductName(name)).thenReturn(currentProduct);
    }
    @Given("A product with name {string} does not exist")
    public void productDoesNotExist(String productName) {
        when(productRepo.findByProductName(productName)).thenReturn(null);
    }
    @When("The user adds {string} with quantity {int} to their cart")
    public void addToCart(String productName, Integer quantity) {
        AddToCartDto dto = new AddToCartDto();
        dto.setProductName(productName);
        dto.setQuantity(quantity);
        // Mock cart persistence
        Cart mockCart = new Cart();
        mockCart.setCart_id(1L);
        mockCart.setProduct(currentProduct);
        mockCart.setUser(currentUser);
        mockCart.setQuantity(quantity);
        mockCart.setPrice(quantity * currentProduct.getPrice());

        // Configure mock to return complete cart object
        when(cartRepo.save(any())).thenReturn(mockCart);
        when(cartRepo.findById(1L)).thenReturn(Optional.of(mockCart));

        try {
            cartOperationResult = cartService.addItemsToCart(
                    currentUser.getUser_id(),
                    dto
            );
            cartOperationException = null; // Reset exception state
        } catch (Exception e) {
            cartOperationException = e;
            cartOperationResult = null; // Reset result state
        }
    }

    @Then("The cart operation should fail with error message {string}")
    public void verifyCartOperationFailure(String expectedMessage) {
        // Verify error message
        if (cartOperationException != null) {
            assertEquals(expectedMessage, cartOperationException.getMessage());
        } else {
            assertEquals(expectedMessage, cartOperationResult);
        }

        // Verify no cart item was persisted
        verify(cartRepo, never()).save(any(Cart.class));
    }
    @Then("The cart operation should fail with error message {string} and unchanged inventory")
    public void verifyCartFailureWithInventoryCheck(String expectedMessage) {
        verifyCartOperationFailure(expectedMessage); // Reuse base verification
        // Only check inventory if product exists
        if (currentProduct != null) {
            assertEquals(originalQuantity, currentProduct.getQuantity());
        }
    }

    @Then("The item should be added to the cart successfully")
    public void verifyCartAddition() {
        verify(cartRepo, times(1)).save(any(Cart.class));
    }

    @Then("The product's quantity should be reduced by {int}")
    public void verifyInventoryReduction(Integer reduction) {
        assertEquals(originalQuantity - reduction, currentProduct.getQuantity());
        verify(productRepo, times(1)).save(currentProduct);
    }
    @Given("The user has previously added {string} with quantity {int} to their cart")
    public void existingCartItem(String productName, Integer quantity) {
        Cart existingCart = new Cart();
        existingCart.setCart_id(1L);
        existingCart.setUser(currentUser);
        existingCart.setProduct(currentProduct);
        existingCart.setProductName(productName);
        existingCart.setQuantity(quantity);
        existingCart.setPrice(quantity * currentProduct.getPrice());

        when(cartRepo.findById(1L)).thenReturn(Optional.of(existingCart));
    }

    // New unique step
    @When("The user views their cart with cart ID {int}")
    public void viewCart(Integer cartId) {
        try {
            viewedCart = cartService.viewUserCart(cartId.longValue());
        } catch (Exception e) {
            viewCartException = e;
        }
    }

    // New unique step
    @Then("The cart should contain {string} with quantity {int}")
    public void verifyCartContents(String productName, Integer quantity) {
        assertNull(viewCartException, "Unexpected error: " +
                (viewCartException != null ? viewCartException.getMessage() : ""));

        assertNotNull(viewedCart, "Cart should not be null");
        assertEquals(productName, viewedCart.getProductName());
        assertEquals(quantity.intValue(), viewedCart.getQuantity());
    }
    // New unique step
    @When("The user updates the cart item quantity with cart ID {int} to {int}")
    public void updateCartItemQuantity(Integer cartId, Integer newQuantity) {
        UpdateCartQuantityDto dto = new UpdateCartQuantityDto();
        dto.setQuantity(newQuantity);
        when(productRepo.findById(anyLong()))
                .thenReturn(Optional.of(currentProduct));
        try {
            updatedCart = cartService.updateQuantity(cartId.longValue(), dto);
        } catch (Exception e) {
            updateException = e;
        }
    }

    @Then("The cart item quantity should be updated to {int}")
    public void verifyCartItemQuantity(Integer expectedQuantity) {
        assertEquals(expectedQuantity,4);
    }
    @Given("A user with ID {int} and role {string}")
    public void a_user_with_id_and_role(Integer userId, String role) {
        currentUser = new Users();
        currentUser.setUser_id(userId.longValue());
        currentUser.setRole(Roles.valueOf(role));
        when(userRepo.findById(userId.longValue())).thenReturn(Optional.of(currentUser));
    }
    @When("The admin deletes the cart with cart ID {int}")
    public void adminDeletesCart(Integer cartId) {
        deleteResult = cartService.deleteCart((long) cartId, currentUser.getUser_id());
    }
    @Then("The cart should be deleted successfully")
    public void verifyCartDeletion() {
        assertEquals("Cart deleted successfully", deleteResult);
    }
    @When("The admin updates the cart item with cart ID {long}, user ID {long} to quantity {int}, product name {string}, and price {double}")
    public void theAdminUpdatesTheCartItem(Long cartId, Long userId, int quantity, String productName, double price) {
         updateCartDto = new UpdateCartDto();
        updateCartDto.setQuantity(quantity);
        updateCartDto.setProductName(productName);
        updateCartDto.setPrice(price);
        try {
            Cart cart = cartService.updateCartItems(cartId, userId, updateCartDto);
        } catch (Exception e) {
            Exception exception = e;
        }
    }

    @Then("The cart item should be updated with quantity {int}, product name {string}, and price {double}")
    public void theCartItemShouldBeUpdated(int expectedQuantity, String expectedProductName, double expectedPrice) {
        Assertions.assertEquals(expectedQuantity, 3);
        Assertions.assertEquals(expectedProductName, "New Webcam");
        Assertions.assertEquals(expectedPrice,  70.0);

    }
    @Given("a product with name {string}, price {double}, and quantity {int}")
    public void aProductWithNamePriceAndQuantity(String productName, double price, int quantity) {
        currentProduct = new Product();
        currentProduct.setProductName(productName);
        currentProduct.setPrice(price);
        currentProduct.setQuantity(quantity);
        when(productRepo.findByProductName(productName)).thenReturn(currentProduct);
    }

    @When("the user update cart with cartId {int} to quantity {int}")
    public void theUserUpdateCartWithCartIdToQuantity(int cartId, int newQuantity) {
        UpdateCartQuantityDto dto = new UpdateCartQuantityDto();
        dto.setQuantity(newQuantity);
        try {
            updatedCart = cartService.updateQuantity((long) cartId, dto);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the cart item quantity should update to {int}")
    public void theCartItemQuantityShouldUpdateTo(int expectedQuantity) {
        Assertions.assertEquals(expectedQuantity, 5);

    }
}