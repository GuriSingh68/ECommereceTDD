package EGEN5203.EcommerceTDD.stepDefinitions;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.model.Cart;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.CartRepo;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import EGEN5203.EcommerceTDD.service.CartService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceStepDefinitions {
    @InjectMocks
    CartService cartService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ProductRepo productRepo;
    @Mock
    private CartRepo cartRepo;

    private Users user;
    private Long userId;
    private Product product;
    private String resultMessage;
    private Cart cartItem;
    private int initialProductQuantity;
    public CartServiceStepDefinitions() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }
    @Given("A user with ID {long}")
    public void aUserWithID(Long userId) {
        user = new Users();
        user.setUser_id(userId);
        this.userId=userId;
        when(userRepo.save(user)).thenReturn(user);
    }
    @Given("A product with name {string}, price {double}, and quantity {int}")
    public void aProductWithNamePriceAndQuantity(String productName, double price, int quantity) {
        product = new Product();
        product.setProductName(productName);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setProduct_id(1L);
        productRepo.save(product);
        this.initialProductQuantity = quantity;
    }
    @When("The user adds {string} with quantity {int} to their cart")
    @Transactional
    public void theUserAddsWithQuantityToTheirCart(String productName, int quantity) {
        when(userRepo.findById(userId)).thenReturn(Optional.ofNullable(user));
        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setProductName(productName);
        addToCartDto.setQuantity(quantity);
        resultMessage = cartService.addItemsToCart(userId, addToCartDto);
       when(productRepo.findByProductName(productName)).thenReturn(product);
        cartItem=cartRepo.findById(product.getProduct_id()).orElseThrow(
                () ->new IllegalArgumentException("Cart not found"));
    }

    @Then("The item should be added to the cart successfully")
    public void theItemShouldBeAddedToTheCartSuccessfully() {
        assertEquals("Item added to cart successfully!", resultMessage);
        assertNotNull(cartItem);
    }

    @Then("The product's quantity should be reduced by {int}")
    public void theProductSQuantityShouldBeReducedBy(int quantityReduced) {
        Product updatedProduct = productRepo.findByProductName(product.getProductName());
        assertEquals(initialProductQuantity - quantityReduced, updatedProduct.getQuantity());
    }
}

