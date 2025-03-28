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

    @Test
    void testAddProductAsAdmin() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setName("Test Product");
        addProductsDto.setPrice(100.0);
        addProductsDto.setStock(10);

        Users adminUser  = new Users();
        adminUser .setEmail(username);
        adminUser .setRole(Roles.ADMIN);

        when(userRepo.findByEmail(username)).thenReturn(adminUser );
        when(productRepo.existsByName(addProductsDto.getName())).thenReturn(false); // Product does not exist

        // Act
        String result = productService.addProducts(username, addProductsDto);

        // Assert
        assertEquals("{\"message\": \"Product added successfully!\"}", result);
    }

    @Test
    void testAddProductAsNonAdmin() {
        // Arrange
        String username = "user@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setName("Test Product");
        addProductsDto.setPrice(100.0);
        addProductsDto.setStock(10);

        Users regularUser  = new Users();
        regularUser .setEmail(username);
        regularUser .setRole(Roles.USER);

        when(userRepo.findByEmail(username)).thenReturn(regularUser );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, addProductsDto));
        assertEquals("Only admin can add products", exception.getMessage());
    }

    @Test
    void testAddProductWithNullProductName() {
        // Arrange
        String username = "admin@example.com";
        AddProductsDto addProductsDto = new AddProductsDto();
        addProductsDto.setName(null); // Invalid product name
        addProductsDto.setPrice(100.0);
        addProductsDto.setStock(10);

        Users adminUser  = new Users();
        adminUser .setEmail(username);
        adminUser .setRole(Roles.ADMIN);

        lenient().when(userRepo.findByEmail(username)).thenReturn(adminUser );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.addProducts(username, addProductsDto));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testUpdateProductAsAdmin() {
        // Arrange
        Long productId = 1L;
        String username = "admin@example.com";
        UpdateProductsDto updateProductsDto = new UpdateProductsDto();
        updateProductsDto.setName("Updated Product");
        updateProductsDto.setPrice(150.0);
        updateProductsDto.setStock(20);

        Users adminUser  = new Users();
        adminUser .setEmail(username);
        adminUser .setRole(Roles.ADMIN);

        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setPrice(100.0);
        existingProduct.setStock(10);

        when(userRepo.findByEmail(username)).thenReturn(adminUser );
        when(productRepo.findById(productId)).thenReturn(java.util.Optional.of(existingProduct));

        // Act
        String result = productService.updateProduct(productId, username, updateProductsDto);

        // Assert
        assertEquals("{\"message\": \"Product updated successfully!\"}", result);
        assertEquals("Updated Product", existingProduct.getName());
        assertEquals(150.0, existingProduct.getPrice());
        assertEquals(20, existingProduct.getStock());
    }

    @Test
    void testUpdateProductAsNonAdmin() {
        // Arrange
        Long productId = 1L;
        String username = "user@example.com";
        UpdateProductsDto updateProductsDto = new UpdateProductsDto();
        updateProductsDto.setName("Updated Product");
        updateProductsDto.setPrice(150.0);
        updateProductsDto.setStock(20);

        Users regularUser  = new Users();
        regularUser .setEmail(username);
        regularUser .setRole(Roles.USER);

        when(userRepo.findByEmail(username)).thenReturn(regularUser );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(productId, username, updateProductsDto));
        assertEquals("Only admin can update products", exception.getMessage());
    }

    @Test
    void testUpdateProductWithNullProductName() {
        // Arrange
        Long productId = 1L;
        String username = "admin@example.com";
        UpdateProductsDto updateProductsDto = new UpdateProductsDto();
        updateProductsDto.setName(null); // Invalid product name
        updateProductsDto.setPrice(150.0);
        updateProductsDto.setStock(20);

        Users adminUser  = new Users();
        adminUser.setEmail(username);
        adminUser.setRole(Roles.ADMIN);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(productId, username, updateProductsDto));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testDeleteProductAsAdmin() {
        // Arrange
        Long productId = 1L;
        String username = "admin@example.com";
        Users adminUser  = new Users();
        adminUser .setEmail(username);
        adminUser .setRole(Roles.ADMIN);

        Product product = new Product();
        product.setProductId(productId);
        product.setName("Test Product");

        lenient().when(userRepo.findByEmail(username)).thenReturn(adminUser ); // Mocking findByEmail
        lenient().when(productRepo.findById(productId)).thenReturn(java.util.Optional.of(product)); // Mocking findById

        // Act
        String result = productService.deleteProduct(productId, username);

        // Assert
        assertEquals("{\"message\": \"Product deleted successfully!\"}", result);
    }

    @Test
    void testDeleteProductAsNonAdmin() {
        // Arrange
        Long productId = 1L;
        String username = "user@example.com";
        Users regularUser  = new Users();
        regularUser .setEmail(username);
        regularUser .setRole(Roles.USER);

        when(userRepo.findByEmail(username)).thenReturn(regularUser ); // Mocking findByEmail

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productService.deleteProduct(productId, username));
        assertEquals("Only admin can delete products", exception.getMessage());
    }
}