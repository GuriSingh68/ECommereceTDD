package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
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
    private AddProductsDto validUpdateDto;
    private Users adminUser;
    private Users nonAdminUser;
    private Product existingProduct;

    @BeforeEach
    void setUp() {
        validUpdateDto = new AddProductsDto();
        validUpdateDto.setProductName("Updated Product");
        validUpdateDto.setPrice(25.99);
        validUpdateDto.setQuantity(50);

        adminUser = new Users();
        adminUser.setUser_id(1L);
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Roles.ADMIN);

        nonAdminUser = new Users();
        nonAdminUser.setUser_id(2L);
        nonAdminUser.setEmail("user@example.com");
        nonAdminUser.setRole(Roles.USER);

        existingProduct = new Product();
        existingProduct.setProduct_id(10L);
        existingProduct.setProductName("Original Product");
        existingProduct.setPrice(19.99);
        existingProduct.setQuantity(100);
    }
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
    //Admin Updating Product
    @Test
    void updateProduct_validAdminUser_productFound_success() {
        // Arrange
        when(userRepo.findByEmail("admin@example.com")).thenReturn(adminUser);
        when(productRepo.findById(10L)).thenReturn(Optional.of(existingProduct));
        when(productRepo.save(any(Product.class))).thenReturn(existingProduct);

        // Act
        String result = productService.updateProduct(10L, "admin@example.com", validUpdateDto);

        // Assert
        assertEquals("Product updated successfully!", result);
        assertEquals("Updated Product", existingProduct.getProductName());
        assertEquals(25.99, existingProduct.getPrice());
        assertEquals(50, existingProduct.getQuantity());

    }
    @Test
    void updateProduct_validAdminUser_productNotFound_throwsException() {
        // Arrange
        when(userRepo.findByEmail("admin@example.com")).thenReturn(adminUser);
        when(productRepo.findById(10L)).thenReturn(Optional.empty());

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(10L, "admin@example.com", validUpdateDto)
        );
        assertEquals("Product not found", exception.getMessage());

    }

    @Test
    void updateProduct_nonAdminUser_throwsException() {
        // Arrange
        when(userRepo.findByEmail("user@example.com")).thenReturn(nonAdminUser);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(10L, "user@example.com", validUpdateDto)
        );
        assertEquals("Only admin can update products", exception.getMessage());

    }

    @Test
    void updateProduct_userNotFound_throwsException() {
        // Arrange
        when(userRepo.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(10L, "nonexistent@example.com", validUpdateDto)
        );
        assertEquals("User  not found", exception.getMessage());

    }


    @Test
    void updateProduct_emptyProductName_throwsException() {
        // Arrange
        validUpdateDto.setProductName("");
        when(userRepo.findByEmail("admin@example.com")).thenReturn(adminUser);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(10L, "admin@example.com", validUpdateDto)
        );
        assertEquals("Product name cannot be empty", exception.getMessage());

    }

    @Test
    void updateProduct_nullProductName_throwsException() {
        // Arrange
        validUpdateDto.setProductName(null);
        when(userRepo.findByEmail("admin@example.com")).thenReturn(adminUser);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(10L, "admin@example.com", validUpdateDto)
        );
        assertEquals("Product name cannot be null or empty", exception.getMessage());

    }

    @Test
    void updateProduct_negativePrice_throwsException() {
        // Arrange
        validUpdateDto.setPrice(-5.0);
        when(userRepo.findByEmail("admin@example.com")).thenReturn(adminUser);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(10L, "admin@example.com", validUpdateDto)
        );
        assertEquals("Price must be greater than zero", exception.getMessage()); // Assuming this is the validation message

    }

    @Test
    void updateProduct_negativeQuantity_throwsException() {
        // Arrange
        validUpdateDto.setQuantity(-10);
        when(userRepo.findByEmail("admin@example.com")).thenReturn(adminUser);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(10L, "admin@example.com", validUpdateDto)
        );
        assertEquals("Quantity must be greater than zero", exception.getMessage()); // Assuming this is the validation message

    }

}