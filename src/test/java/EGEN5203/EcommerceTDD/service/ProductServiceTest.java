package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.dto.UpdateProductsDto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private ProductService productService;

    // Test adding a product as an admin user
    @Test
    void testAddProductAsAdmin() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setName("Test Product");
        addProductsDto.setPrice(100.0);
        addProductsDto.setStock(10);

        Users adminUser  = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        // Mock admin user and non-existing product
        when(userRepo.findByEmail(username)).thenReturn(adminUser);
        when(productRepo.existsByName(addProductsDto.getName())).thenReturn(false);

        // Act
        String result = productService.addProducts(username, addProductsDto);

        // Assert
        assertEquals("{\"message\": \"Product added successfully!\"}", result);
    }

    // Test adding a product as a non-admin user should throw exception
    @Test
    void testAddProductAsNonAdmin() {
        // Arrange
        String username = "user@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setName("Test Product");
        addProductsDto.setPrice(100.0);
        addProductsDto.setStock(10);

        Users regularUser  = new Users();
        regularUser.setEmail(username);
        regularUser.setRole(Roles.USER);

        when(userRepo.findByEmail(username)).thenReturn(regularUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, addProductsDto));
        assertEquals("Only admin can add products", exception.getMessage());
    }

    // Test adding a product with null name should throw exception
    @Test
    void testAddProductWithNullProductName() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setName(null);
        addProductsDto.setPrice(100.0);
        addProductsDto.setStock(10);

        Users adminUser  = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        lenient().when(userRepo.findByEmail(username)).thenReturn(adminUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, addProductsDto));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    // Test successful update by admin
    @Test
    void testUpdateProductAsAdmin() {
        // Arrange
        Long productId = 1L;
        String username = "admin@example.com";
        UpdateProductsDto updateProductsDto = new UpdateProductsDto();
        updateProductsDto.setName("Updated Product");
        updateProductsDto.setPrice(150.0);
        updateProductsDto.setStock(20);

        Users adminUser = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setPrice(100.0);
        existingProduct.setStock(10);

        when(userRepo.findByEmail(username)).thenReturn(adminUser);
        when(productRepo.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Act
        String result = productService.updateProduct(productId, username, updateProductsDto);

        // Assert
        assertEquals("{\"message\": \"Product updated successfully!\"}", result);
        assertEquals("Updated Product", existingProduct.getName());
        assertEquals(150.0, existingProduct.getPrice());
        assertEquals(20, existingProduct.getStock());
    }

    // Test update by non-admin user should fail
    @Test
    void testUpdateProductAsNonAdmin() {
        // Arrange
        Long productId = 1L;
        String username = "user@example.com";
        UpdateProductsDto updateProductsDto = new UpdateProductsDto();
        updateProductsDto.setName("Updated Product");
        updateProductsDto.setPrice(150.0);
        updateProductsDto.setStock(20);

        Users regularUser = new Users();
        regularUser.setEmail(username);
        regularUser.setRole(Roles.USER);

        when(userRepo.findByEmail(username)).thenReturn(regularUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(productId, username, updateProductsDto));
        assertEquals("Only admin can update products", exception.getMessage());
    }

    // Test updating with null product name should fail
    @Test
    void testUpdateProductWithNullProductName() {
        // Arrange
        Long productId = 1L;
        String username = "admin@example.com";
        UpdateProductsDto updateProductsDto = new UpdateProductsDto();
        updateProductsDto.setName(null); // Invalid name
        updateProductsDto.setPrice(150.0);
        updateProductsDto.setStock(20);

        Users adminUser = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(productId, username, updateProductsDto));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    // Test successful product deletion by admin
    @Test
    void testDeleteProductAsAdmin() {
        // Arrange
        Long productId = 1L;
        String username = "admin@example.com";
        Users adminUser = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        Product product = new Product();
        product.setProductId(productId);
        product.setName("Test Product");

        lenient().when(userRepo.findByEmail(username)).thenReturn(adminUser);
        lenient().when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        lenient().when(productRepo.existsById(productId)).thenReturn(true);

        // Act
        String result = productService.deleteProduct(productId, username);

        // Assert
        assertEquals("Product deleted successfully!", result);
    }

    // Test delete by non-admin user should fail
    @Test
    void testDeleteProductAsNonAdmin() {
        // Arrange
        Long productId = 1L;
        String username = "user@example.com";
        Users regularUser = new Users();
        regularUser.setEmail(username);
        regularUser.setRole(Roles.USER);

        when(userRepo.findByEmail(username)).thenReturn(regularUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.deleteProduct(productId, username));
        assertEquals("Only admin can delete products", exception.getMessage());
    }

    // Test product validation success
    @Test
    void testValidateAddProductInput_HappyFlow() {
        // Arrange
        AddProductsDto validDto = new AddProductsDto();
        validDto.setName("Awesome Gadget");
        validDto.setDescription("A revolutionary new gadget");
        validDto.setPrice(99.99);
        validDto.setStock(100);
        validDto.setCategory("Electronics");
        validDto.setImageUrl("http://example.com/gadget.jpg");
        validDto.setTags(Arrays.asList("new", "tech", "cool"));
        validDto.setAdmin("admin123");

        // Act & Assert
        assertDoesNotThrow(() -> productService.validateAddProductInput(validDto));
    }

    // Test fetching all products returns expected list
    @Test
    void testFetchAllProducts_ReturnsListOfProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Product A");

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setName("Product B");

        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(productRepo.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.fetchAllProducts();

        // Assert
        assertEquals(expectedProducts.size(), actualProducts.size());
        assertEquals(expectedProducts, actualProducts);
    }

    // Test when no products exist
    @Test
    void testFetchAllProducts_ReturnsEmptyListWhenNoProductsExist() {
        // Arrange
        when(productRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Product> actualProducts = productService.fetchAllProducts();

        // Assert
        assertTrue(actualProducts.isEmpty());
    }

    // Test product addition when user is not found
    @Test
    void testAddProduct_UserNotFound() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto dto = new AddProductsDto();
        dto.setName("Product X");
        dto.setPrice(100.0);
        dto.setStock(10);

        when(userRepo.findByEmail(username)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, dto));
        assertEquals("User not found", exception.getMessage());
    }

    // Test adding a product that already exists
    @Test
    void testAddProduct_ProductAlreadyExists() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto dto = new AddProductsDto();
        dto.setName("Duplicate Product");
        dto.setPrice(50.0);
        dto.setStock(5);

        Users adminUser = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        when(userRepo.findByEmail(username)).thenReturn(adminUser);
        when(productRepo.existsByName(dto.getName())).thenReturn(true);

        // Act
        String result = productService.addProducts(username, dto);

        // Assert
        assertEquals("Product already exists. Add a different product.", result);
    }

    // Test updating a product that doesn't exist
    @Test
    void testUpdateProduct_ProductNotFound() {
        // Arrange
        Long productId = 100L;
        String username = "admin@example.com";
        UpdateProductsDto dto = new UpdateProductsDto();
        dto.setName("Updated");
        dto.setPrice(99.0);
        dto.setStock(10);

        Users adminUser = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        when(userRepo.findByEmail(username)).thenReturn(adminUser);
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(productId, username, dto));
        assertEquals("Product not found", exception.getMessage());
    }

    // Test delete when user is not found
    @Test
    void testDeleteProduct_UserNotFound() {
        // Arrange
        Long productId = 1L;
        String username = "admin@example.com";

        when(userRepo.findByEmail(username)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.deleteProduct(productId, username));
        assertEquals("User not found", exception.getMessage());
    }

    // Test delete when product is not found
    @Test
    void testDeleteProduct_ProductNotFound() {
        // Arrange
        Long productId = 1L;
        String username = "admin@example.com";

        Users adminUser = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        when(userRepo.findByEmail(username)).thenReturn(adminUser);
        when(productRepo.existsById(productId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.deleteProduct(productId, username));
        assertEquals("Product not found", exception.getMessage());
    }

    // Test validation failure for invalid price and stock
    @Test
    void testValidateUpdateProductInput_InvalidPriceAndStock() {
        UpdateProductsDto dto = new UpdateProductsDto();
        dto.setName("Item");
        dto.setPrice(-5.0); // Invalid
        dto.setStock(-1);   // Invalid

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(1L, "admin@example.com", dto));
        assertEquals("Price must be greater than zero", ex.getMessage());
    }

    // Test add product with null name
    @Test
    void testAddProduct_InvalidName_Null() {
        String username = "admin@example.com";
        AddProductsDto dto = new AddProductsDto();
        dto.setName(null); // Invalid name
        dto.setPrice(10.0);
        dto.setStock(5);

        Users adminUser = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, dto));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    // Test add product with invalid price
    @Test
    void testAddProduct_InvalidPrice_Zero() {
        String username = "admin@example.com";
        AddProductsDto dto = new AddProductsDto();
        dto.setName("Valid Name");
        dto.setPrice(0.0); // Invalid
        dto.setStock(5);

        Users adminUser = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, dto));
        assertEquals("Price must be greater than zero", exception.getMessage());
    }

}