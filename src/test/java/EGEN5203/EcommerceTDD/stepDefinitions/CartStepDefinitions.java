package EGEN5203.EcommerceTDD.stepDefinitions;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.dto.ProductDto;
import EGEN5203.EcommerceTDD.dto.UpdateCartDto;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartStepDefinitions {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CartService cartService;

    private Cart createdCart;
    private List<Cart> foundCarts;
    private Cart foundCart;

    public CartStepDefinitions() {
        MockitoAnnotations.openMocks(this);
    }


    @Given("the following products exist:")
    public void theFollowingProductsExist(List<Map<String, String>> productsData) {
        for (Map<String, String> productMap : productsData) {
            Product product = new Product();
            product.setProductId(Long.parseLong(productMap.get("productId")));
            product.setName(productMap.get("name")); // Corrected key: "name" instead of "productName"
            // Assuming these fields might exist in your Product entity
            if (productMap.containsKey("description")) {
                product.setDescription(productMap.get("description"));
            }
            if (productMap.containsKey("price")) {
                product.setPrice(Double.parseDouble(productMap.get("price")));
            }
            if (productMap.containsKey("stock")) {
                product.setStock(Integer.parseInt(productMap.get("stock")));
            }

            when(productRepo.findById(product.getProductId())).thenReturn(Optional.of(product));
        }
    }

    @When("the client creates a cart for customer {string} with products:")
    public void theClientCreatesCartForCustomerWithProducts(String customerId, List<Map<String, String>> productData) {
        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setCustomerId(Long.parseLong(customerId));
        List<ProductDto> productDtos = productData.stream()
                .map(map -> {
                    ProductDto dto = new ProductDto();
                    dto.setProductId(Long.parseLong(map.get("productId")));
                    dto.setQuantity(Integer.parseInt(map.get("quantity")));
                    return dto;
                }).toList();
        addToCartDto.setProducts(productDtos);
        createdCart = cartService.createCart(addToCartDto);
    }

    @Then("the cart should be created")
    public void theCartShouldBeCreated() {
        assertNull(createdCart);
        verify(cartRepo).save(any(Cart.class));
    }

    @Given("some carts exist")
    public void someCartsExist() {
        Cart cart = new Cart();
        cart.setId(1L);
        when(cartRepo.findAll()).thenReturn(List.of(cart));
    }

    @When("the client retrieves all carts")
    public void theClientRetrievesAllCarts() {
        foundCarts = cartService.findAll();
    }

    @Then("all carts should be returned")
    public void allCartsShouldBeReturned() {
        assertNotNull(foundCarts);
        assertFalse(foundCarts.isEmpty());
    }

    @Given("a cart with ID {string} exists")
    public void aCartWithIDExists(String cartId) {
        Cart cart = new Cart();
        cart.setId(Long.parseLong(cartId));
        when(cartRepo.findById(Long.parseLong(cartId))).thenReturn(Optional.of(cart));
    }

    @When("the client retrieves cart {string}")
    public void theClientRetrievesCart(String cartId) {
        foundCart = cartService.findOne(Long.parseLong(cartId));
    }

    @Then("cart {string} should be returned")
    public void cartShouldBeReturned(String cartId) {
        assertNotNull(foundCart);
        assertEquals(Long.parseLong(cartId), foundCart.getId());
    }

    @When("the client updates cart {string} with products:")
    public void theClientUpdatesCartWithProducts(String cartId, List<Map<String, String>> productData) {
        UpdateCartDto dto = new UpdateCartDto();
        List<ProductDto> productDtos = productData.stream()
                .map(map -> {
                    ProductDto productDto = new ProductDto();
                    productDto.setProductId(Long.parseLong(map.get("productId")));
                    productDto.setQuantity(Integer.parseInt(map.get("quantity")));
                    return productDto;
                }).toList();
        dto.setProducts(productDtos);
        cartService.updateCart(Long.parseLong(cartId), dto);
    }

    @Then("the cart should be updated")
    public void theCartShouldBeUpdated() {
        verify(cartRepo).save(any(Cart.class));
    }

    @Given("a cart with ID {string} exists for customer {string}")
    public void aCartWithIDExistsForCustomer(String cartId, String customerId) {
        Cart cart = new Cart();
        cart.setId(Long.parseLong(cartId));
        Users customer = new Users();
        customer.setUser_id(Long.parseLong(customerId));
        cart.setCustomer(customer);
        when(cartRepo.findById(Long.parseLong(cartId))).thenReturn(Optional.of(cart));
    }

    @Given("a customer with ID {string} exists")
    public void aCustomerWithIDExistsForUpdate(String customerId) {
        Users customer = new Users();
        customer.setUser_id(Long.parseLong(customerId));
        when(userRepo.findById(Long.parseLong(customerId))).thenReturn(Optional.of(customer));
    }

    @When("the client updates cart {string} to customer {string}")
    public void theClientUpdatesCartToCustomer(String cartId, String newCustomerId) {
        UpdateCartDto dto = new UpdateCartDto();
        dto.setCustomerId(Long.parseLong(newCustomerId));
        cartService.updateCart(Long.parseLong(cartId), dto);
    }

    @Then("the cart should be updated with new customer")
    public void theCartShouldBeUpdatedWithNewCustomer() {
        verify(cartRepo).save(any(Cart.class));
    }

    @When("the client deletes cart {string}")
    public void theClientDeletesCart(String cartId) {
        when(cartRepo.findById(Long.parseLong(cartId))).thenReturn(Optional.of(new Cart()));
        cartService.deleteCart(Long.parseLong(cartId));
    }

    @Then("the cart should be removed")
    public void theCartShouldBeRemoved() {
        verify(cartRepo).deleteById(anyLong());
    }
}