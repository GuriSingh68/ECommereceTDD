package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.dto.UpdateProductsDto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing product-related operations.
 * Contains business logic for adding, updating, deleting, and fetching products.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    /**
     * Adds a new product to the system.
     *
     * @param username the username of the admin adding the product.
     * @param addProductsDTO the data transfer object containing product details.
     * @return a message indicating the result of the operation.
     */
    public String addProducts(String username, AddProductsDto addProductsDTO) {
        // Validate input
        validateAddProductInput(addProductsDTO);

        // Find user by email
        Users user = userRepo.findByEmail(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Check if the user has admin role
        if (!user.getRole().equals(Roles.ADMIN)) {
            throw new IllegalArgumentException("Only admin can add products");
        }

        // Check if the product already exists
        Boolean productExist = productRepo.existsByName(addProductsDTO.getName());
        if (productExist) {
            return "Product already exists. Add a different product.";
        }

        // Create and save the product
        Product product = new Product();
        product.setName(addProductsDTO.getName());
        product.setDescription(addProductsDTO.getDescription());
        product.setPrice(addProductsDTO.getPrice());
        product.setStock(addProductsDTO.getStock());
        product.setCategory(addProductsDTO.getCategory());
        product.setImageUrl(addProductsDTO.getImageUrl());
        product.setUsers(user);
        productRepo.save(product);

        return "{\"message\": \"Product added successfully!\"}";
    }

    /**
     * Updates an existing product's details.
     *
     * @param id the ID of the product to update.
     * @param username the username of the admin updating the product.
     * @param updateProductsDTO the data transfer object containing updated product details.
     * @return a message indicating the result of the operation.
     */
    public String updateProduct(Long id, String username, UpdateProductsDto updateProductsDTO) {
        // Validate input
        System.out.println(updateProductsDTO);
        validateUpdateProductInput(updateProductsDTO);

        // Find user by email
        Users user = userRepo.findByEmail(username);
        if (user == null) {
            throw new IllegalArgumentException("User  not found");
        }

        // Check if the user has admin role
        if (!user.getRole().equals(Roles.ADMIN)) {
            throw new IllegalArgumentException("Only admin can update products");
        }

        // Find the product by ID
        Product product = productRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Update product details
        product.setName(updateProductsDTO.getName());
        product.setPrice(updateProductsDTO.getPrice());
        product.setStock(updateProductsDTO.getStock());
        product.setCategory(updateProductsDTO.getCategory());
        product.setImageUrl(updateProductsDTO.getImageUrl());
        product.setDescription(updateProductsDTO.getDescription());
        productRepo.save(product);

        return "{\"message\": \"Product updated successfully!\"}";
    }

    /**
     * Deletes a product from the system.
     *
     * @param id the ID of the product to delete.
     * @param username the username of the admin deleting the product.
     * @return a message indicating the result of the operation.
     */
    public String deleteProduct(Long id, String username) {
        // Find user by email
        Users user = userRepo.findByEmail(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Check if the user has admin role
        if (!user.getRole().equals(Roles.ADMIN)) {
            throw new IllegalArgumentException("Only admin can delete products");
        }

        if (!productRepo.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }

        // Delete the product by ID
        productRepo.deleteById(id);
        return "Product deleted successfully!";

    }

    /**
     * Validates the input for adding a new product.
     *
     * @param addProductsDTO the data transfer object containing product details.
     * @throws IllegalArgumentException if validation fails.
     */
    void validateAddProductInput(AddProductsDto addProductsDTO) {
        if (addProductsDTO.getName() == null || addProductsDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (addProductsDTO.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (addProductsDTO.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }

    /**
     * Validates the input for updating an existing product.
     *
     * @param updateProductsDTO the data transfer object containing updated product details.
     * @throws IllegalArgumentException if validation fails.
     */
    private void validateUpdateProductInput(UpdateProductsDto updateProductsDTO) {
        if (updateProductsDTO.getName() == null || updateProductsDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (updateProductsDTO.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (updateProductsDTO.getStock() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    public List<Product> fetchAllProducts() {
        return productRepo.findAll();
    }
}