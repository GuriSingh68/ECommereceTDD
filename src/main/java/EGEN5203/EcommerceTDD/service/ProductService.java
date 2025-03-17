package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    public String addProducts(String userid, AddProductsDto addProductsDTO) {
        // Validate input
        validateAddProductInput(addProductsDTO);

        // Find user by email
        Users user = userRepo.findByEmail(userid);
        if (user == null) {
            throw new IllegalArgumentException("User  not found");
        }

        // Check if the user has admin role
        if (!user.getRole().equals(Roles.ADMIN)) {
            throw new IllegalArgumentException("Only admin can add products");
        }
        // Create and save the product
        Boolean productExist=productRepo.existsByProductName(addProductsDTO.getProductName());
        if (!productExist)
        {
            Product product = new Product();
            product.setProductName(addProductsDTO.getProductName());
            product.setPrice(addProductsDTO.getPrice());
            product.setQuantity(addProductsDTO.getQuantity());
            product.setDescription(addProductsDTO.getDescription());
            product.setCategory(addProductsDTO.getCategory());
            productRepo.save(product);

            return "Product added successfully!";
        }
        return("Product Already Exist. Add different product");
    }

    public String updateProduct(Long id, String userid, AddProductsDto updateProductsDTO) {
        // Validate input
        validateAddProductInput(updateProductsDTO);

        // Find user by email
        Users user = userRepo.findByEmail(userid);
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
        product.setProductName(updateProductsDTO.getProductName());
        product.setPrice(updateProductsDTO.getPrice());
        product.setQuantity(updateProductsDTO.getQuantity());
        productRepo.save(product);

        return "Product updated successfully!";
    }

    public String deleteProduct(Long id, String userid) {
        // Find user by email
        Users user = userRepo.findByEmail(userid);
        if (user == null) {
            throw new IllegalArgumentException("User  not found");
        }

        // Check if the user has admin role
        if (!user.getRole().equals(Roles.ADMIN)) {
            throw new IllegalArgumentException("Only admin can delete products");
        }

        // Delete the product by ID
        productRepo.deleteById(id);
        return "Product deleted successfully!";
    }

    private void validateAddProductInput(AddProductsDto addProductsDTO) {
        if (addProductsDTO.getProductName() == null || addProductsDTO.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (addProductsDTO.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (addProductsDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    public List<Product> fetchAllProducts() {
        return productRepo.findAll();
    }
}