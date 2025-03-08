package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the ProductService class.
 * This class tests the functionality of adding products to the system,
 * ensuring that only authorized users (admins) can add products,
 * and that the input data is validated correctly.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepo productRepo; // Mocked repository for product data

    @Mock
    private UserRepo userRepo; // Mocked repository for user data

    @InjectMocks
    private ProductService productService; // Service under test

    /**
     * Tests the scenario where an admin user successfully adds a product.
     * It verifies that the product is added and the correct success message is returned.
     */
    @Test
    void testAddProductAsAdmin() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setProductName("Test Product");
        addProductsDto.setPrice(100.0);
        addProductsDto.setQuantity(10);

        Users adminUser  = new Users();
        adminUser .setEmail(username);
        adminUser .setRole(Roles.ADMIN);

        when(userRepo.findByEmail(username)).thenReturn(adminUser );

        // Act
        String result = productService.addProducts(username, addProductsDto);

        // Assert
        assertEquals("Product added successfully!", result);
    }

    /**
     * Tests the scenario where a non-admin user attempts to add a product.
     * It verifies that an exception is thrown with the appropriate error message.
     */
    @Test
    void testAddProductAsNonAdmin() {
        // Arrange
        String username = "user@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setProductName("Test Product");
        addProductsDto.setPrice(100.0);
        addProductsDto.setQuantity(10);

        Users regularUser  = new Users();
        regularUser .setEmail(username);
        regularUser .setRole(Roles.USER);

        when(userRepo.findByEmail(username)).thenReturn(regularUser );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, addProductsDto));
        assertEquals("Only admin can add products", exception.getMessage());
    }

    /**
     * Tests the scenario where an admin user attempts to add a product with a null product name.
     * It verifies that an exception is thrown with the appropriate error message.
     */
    @Test
    void testAddProductWithNullProductName() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setProductName(null); // Invalid product name
        addProductsDto.setPrice(100.0);
        addProductsDto.setQuantity(10);

        Users adminUser  = new Users();
        adminUser .setEmail(username);
        adminUser .setRole(Roles.ADMIN);

        lenient().when(userRepo.findByEmail(username)).thenReturn(adminUser );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, addProductsDto));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    /**
     * Tests the scenario where an admin user attempts to add a product with a negative price.
     * It verifies that an exception is thrown with the appropriate error message.
     */
    @Test
    void testAddProductWithNegativePrice() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setProductName("Test Product");
        addProductsDto.setPrice(-50.0); // Invalid price
        addProductsDto.setQuantity(10);

        Users adminUser  = new Users();
        adminUser .setEmail(username);
        adminUser .setRole(Roles.ADMIN);

        lenient().when(userRepo.findByEmail(username)).thenReturn(adminUser );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, addProductsDto));
        assertEquals("Price must be greater than zero", exception.getMessage());
    }

    /**
     * Tests the scenario where an admin user attempts to add a product with a negative quantity.
     * It verifies that an exception is thrown with the appropriate error message.
     */
    @Test
    void testAddProductWithNegativeQuantity() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setProductName("Test Product");
        addProductsDto.setPrice(100.0);
        addProductsDto.setQuantity(-5); // Invalid quantity

        Users adminUser  = new Users();
        adminUser .setEmail(username);
        adminUser .setRole(Roles.ADMIN);

        lenient().when(userRepo.findByEmail(username)).thenReturn(adminUser );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, addProductsDto));
        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }
}