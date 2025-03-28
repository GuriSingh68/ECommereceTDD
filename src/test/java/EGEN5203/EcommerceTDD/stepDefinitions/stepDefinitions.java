package EGEN5203.EcommerceTDD.stepDefinitions;

import EGEN5203.EcommerceTDD.dto.*;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import EGEN5203.EcommerceTDD.service.ProductService;
import EGEN5203.EcommerceTDD.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Ensures mocks are initialized
public class stepDefinitions {

    @Mock
    private UserRepo userRepo;

    @Mock
    private ProductRepo productRepo; // Mocked repository for product data

    @InjectMocks
    private ProductService productService; // Service under test

    private String resultMessage; // Result message from the service


    @InjectMocks
    private UserService userService; // Inject mock repo into service

    private Users user;
    private Logindto logindto;
    private RoledetailsDTO roledetailsDTO;
    private String loginResult;
    private String userRole;
    private String deleteResult;
    private Exception exception;
    private Signupdto signupRequest;
    private String response;

    public stepDefinitions() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Given("A user with email {string} and password {string}")
    public void testLoginForExistingUser(String email, String password) {
        user = new Users();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepo.findByEmail(email)).thenReturn(user);
        when(userRepo.existsByEmail(email)).thenReturn(true);
    }

    @When("The user tries to log in with email {string} and password {string}")
    public void userLoginsWithCred(String email, String password) throws JsonProcessingException {

        logindto = new Logindto();
        logindto.setEmail(email);
        logindto.setPassword(password);

        loginResult = userService.login(logindto);
    }

    @Then("The login should be successful")
    public void successfulLogin() {
        assertEquals("User login successfully", loginResult);
    }

    @Given("No user exists with email {string}")
    public void userNotExists(String email){
        when(userRepo.findByEmail(email)).thenReturn(null);
    }

    @When("The user tries to log in with invalid email {string} and password {string}")
    public void userEntersInvalidCred(String email,String password){
        logindto=new Logindto();
        logindto.setEmail(email);
        logindto.setPassword(password);
        Exception e = Assertions.assertThrows(IllegalArgumentException.class,()->
                userService.login(logindto) );
        loginResult=e.getMessage();
    }
    @Then("The login should fail with an error message {string}")
    public void invalidLoginException(String message){

        assertEquals("User not found",loginResult);
    }
    @Given("an existing user with email {string} and role {string}")
    public void an_existing_user_with_email_and_role(String email, String role) {
        // Create user object with given email and role
        user = new Users();
        user.setEmail(email);
        user.setRole(Roles.valueOf(role));

        // Mock repository behavior
        when(userRepo.findByEmail(email)).thenReturn(user);

        // Create role details DTO
        roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail(email);
        roledetailsDTO.setRole(Roles.valueOf(role));
    }

    @When("the admin updates the role of {string} to {string}")
    public void the_admin_updates_the_role_of_to(String email, String newRole) {
        // Set the new role in the DTO
        roledetailsDTO.setRole(Roles.valueOf(newRole));
        userRole = userService.updateRoles(roledetailsDTO);
    }

    @Then("the system should return {string}")
    public void the_system_should_return(String expectedMessage) {
        // Assert expected message and role update
        assertEquals(expectedMessage, userRole);
        assertEquals(roledetailsDTO.getRole(), user.getRole());
    }

    @Given("User exist in our database with email {string}")
   public void userExistsInOurDatabase(String email){
        Users user=new Users();
        user.setEmail("abc@xyz.com");
        when(userRepo.findByEmail(user.getEmail())).thenReturn(user);
    }
    @When("Admin tries to delete the {string} from our database")
   public void adminDeleteUser(String email){
        RoledetailsDTO roledetailsDTO1=new RoledetailsDTO();
        roledetailsDTO1.setRole(Roles.ADMIN);
        roledetailsDTO1.setEmail("user@xyz.com");
        deleteResult=userService.deleteUsers(email,roledetailsDTO1);
    }
    @Then("the system successfully deletes the user")
   public void successfulDeleteUser(){
        Assertions.assertEquals("User: abc@xyz.com deleted successfully.",deleteResult);
    }

    @When("The user tries to add a product with name {string}, price {double}, and quantity {int}")
    public void the_user_tries_to_add_a_product(String productName, double price, int quantity) {
        // Create a DTO for adding products
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setName(productName); // This can be null
        addProductsDto.setPrice(price);
        addProductsDto.setStock(quantity);

        // Attempt to add the product using the ProductService
        try {
            resultMessage = productService.addProducts(user.getEmail(), addProductsDto);
        } catch (Exception e) {
            exception = e; // Capture any exception that occurs
        }
    }

    // New method to handle the case where product name is null
    @When("The user tries to add a product with name null, price {double}, and quantity {int}")
    public void the_user_tries_to_add_a_product_with_name_null_price_and_quantity(double price, int quantity) {
        // Create a DTO for adding products with a null product name
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setName(null); // Explicitly set to null
        addProductsDto.setPrice(price);
        addProductsDto.setStock(quantity);

        // Attempt to add the product using the ProductService
        try {
            resultMessage = productService.addProducts(user.getEmail(), addProductsDto);
        } catch (Exception e) {
            exception = e; // Capture any exception that occurs
        }
    }

    @Then("The product should be added successfully")
    public void the_product_should_be_added_successfully() {
        Assertions.assertEquals("{\"message\": \"Product added successfully!\"}", resultMessage);
    }

    @Then("The product addition should fail with an error message {string}")
    public void the_product_addition_should_fail_with_an_error_message(String expectedMessage) {
        Assertions.assertNotNull(exception, "Expected an exception but none was thrown.");
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("An existing product with name {string} and price {double}")
    public void an_existing_product_with_name_and_price(String productName, double price) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(price);
        product.setStock(10); // Set a default stock value

        when(productRepo.findById(1L)).thenReturn(java.util.Optional.of(product)); // Mocking findById
    }

    @When("The user tries to update the product with name {string}, price {double}, and quantity {int}")
    public void the_user_tries_to_update_the_product(String productName, double price, int quantity) {
        UpdateProductsDto updateProductDto = new UpdateProductsDto();
        updateProductDto.setName(productName);
        updateProductDto.setPrice(price);
        updateProductDto.setStock(quantity);

        try {
            resultMessage = productService.updateProduct(1L, user.getEmail(), updateProductDto);
        } catch (Exception e) {
            exception = e; // Capture any exception that occurs
        }
    }

    @When("The user tries to update the product with name null, price {double}, and quantity {int}")
    public void the_user_tries_to_update_the_product_with_name_null_price_and_quantity(double price, int quantity) {
        // Create a DTO for updating products with a null product name
        UpdateProductsDto updateProductDto = new UpdateProductsDto();
        updateProductDto.setName(null); // Explicitly set to null
        updateProductDto.setPrice(price);
        updateProductDto.setStock(quantity);

        // Attempt to update the product using the ProductService
        try {
            resultMessage = productService.updateProduct(1L, user.getEmail(), updateProductDto);
        } catch (Exception e) {
            exception = e; // Capture any exception that occurs
        }
    }

    @Then("The product should be updated successfully")
    public void the_product_should_be_updated_successfully() {
        Assertions.assertEquals("{\"message\": \"Product updated successfully!\"}", resultMessage);
    }

    @Then("The product update should fail with an error message {string}")
    public void the_product_update_should_fail_with_an_error_message(String expectedMessage) {
        Assertions.assertNotNull(exception, "Expected an exception but none was thrown.");
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("An existing product with name {string}")
    public void an_existing_product_with_name(String productName) {
        // Create a product object with the given name
        Product product = new Product();
        product.setName(productName);
        product.setPrice(100.0); // Set a default price
        product.setStock(10); // Set a default stock value

        // Mock the behavior of the product repository to return this product when searched by ID
        when(productRepo.existsById(1L)).thenReturn(true); // Mocking findById
    }

    @Given("A user with email {string} and role {string}")
    public void a_user_with_email_and_role(String email, String role) {
        user = new Users();
        user.setEmail(email);
        user.setRole(Roles.valueOf(role)); // Set the role based on the input
        lenient().when(userRepo.findByEmail(email)).thenReturn(user); // Mocking user retrieval
    }


    // Step definitions for deleting products
    @When("The user tries to delete the product")
    public void the_user_tries_to_delete_the_product() {
        try {
            resultMessage = productService.deleteProduct(1L, user.getEmail());
        } catch (Exception e) {
            exception = e; // Capture any exception that occurs
        }
    }

    @Then("The product should be deleted successfully")
    public void the_product_should_be_deleted_successfully() {
        Assertions.assertEquals("{\"message\": \"Product deleted successfully!\"}", resultMessage);
    }

    @When("The user tries to delete a product with ID {int}")
    public void the_user_tries_to_delete_a_product_with_id(int productId) {
        // Mock the behavior of the product repository to return empty for the non-existing product
        lenient().when(productRepo.findById((long) productId)).thenReturn(java.util.Optional.empty());

        try {
            resultMessage = productService.deleteProduct((long) productId, user.getEmail());
        } catch (Exception e) {
            exception = e; // Capture any exception that occurs
        }
    }

    @Then("The product deletion should fail with an error message {string}")
    public void the_product_deletion_should_fail_with_an_error_message(String expectedMessage) {
        Assertions.assertNotNull(exception, "Expected an exception but none was thrown.");
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("A user with email {string} exists")
    public void a_user_with_email_exists(String email) {
        Users existingUser = new Users();
        existingUser.setEmail(email);
        // Mock existsByEmail to return true for existing user
        when(userRepo.existsByEmail(email)).thenReturn(true);
        // No need to mock save() for this case since it shouldn't be called
    }

    @When("The user submits signup request with firstName {string}, lastName {string}, email {string}, phoneNumber {string}, password {string}, and role {string}")
    public void the_user_submits_signup_request(String firstName, String lastName, String email,
                                                String phoneNumber, String password, String role) {
        signupRequest = new Signupdto();
        signupRequest.setFirstName(firstName);
        signupRequest.setLastName(lastName);
        signupRequest.setEmail(email);
        signupRequest.setPhoneNumber(phoneNumber);
        signupRequest.setPassword(password);

        try {
            if (!role.isEmpty()) {
                signupRequest.setRole(Roles.valueOf(role));
            }
            response = userService.userSignup(signupRequest);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("The signup should be successful with message {string}")
    public void the_signup_should_be_successful(String message) {
        assertNull(exception, "No exception should be thrown");
        assertNotNull(response, "Response should not be null");
        assertEquals("{\"message\": \"User signed up successfully!\"}", response);
    }

    @Then("The user {string} should exist with role {string}")
    public void the_user_should_exist_with_role(String email, String role) {
        // Verify the mock was called with the expected email
        verify(userRepo).findByEmail(email);

        // If you need to actually verify the saved user:
        Users user = userRepo.findByEmail(email);
        assertNotNull(user, "User should exist in repository");
        assertEquals(Roles.valueOf(role), user.getRole(), "User role should match");
    }

    @Then("The signup should fail with error {string}")
    public void the_signup_should_fail_with_error(String errorMessage) {
        // For API responses (when service returns error message)
        if (response != null) {
            assertTrue(response.contains(errorMessage),
                    "Expected error message: " + errorMessage + " but got: " + response);
        }
        // For exceptions (when service throws exception)
        else if (exception != null) {
            assertEquals(errorMessage, exception.getMessage());
        }
        // If neither response nor exception contains the error
        else {
            fail("Expected signup to fail with message: " + errorMessage);
        }
    }


}

