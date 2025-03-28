package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.dto.UpdateProductsDto;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing products in the e-commerce application.
 * Provides endpoints for viewing, adding, updating, and deleting products.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Retrieves a list of all products.
     *
     * @return a list of {@link Product} objects.
     */
    @GetMapping
    public List<Product> viewAllProducts() {
        return productService.fetchAllProducts();
    }

    /**
     * Adds a new product to the system.
     *
     * @param addProductsDTO the data transfer object containing product details.
     * @param username the username of the admin adding the product.
     * @return a message indicating the result of the operation.
     */
    @PostMapping("/add/{username}")
    public String addProducts(@RequestBody AddProductsDto addProductsDTO, @PathVariable String username) {
        return productService.addProducts(username, addProductsDTO);
    }

    /**
     * Updates an existing product's details.
     *
     * @param id the ID of the product to update.
     * @param updateProductsDTO the data transfer object containing updated product details.
     * @param username the username of the admin updating the product.
     * @return a message indicating the result of the operation.
     */
    @PatchMapping("/update/{id}/{username}")
    public String updateProduct(@PathVariable Long id, @RequestBody UpdateProductsDto updateProductsDTO, @PathVariable String username) {
        return productService.updateProduct(id, username, updateProductsDTO);
    }

    /**
     * Deletes a product from the system.
     *
     * @param id the ID of the product to delete.
     * @param username the username of the admin deleting the product.
     * @return a message indicating the result of the operation.
     */
    @DeleteMapping("/delete/{id}/{username}")
    public String deleteProduct(@PathVariable Long id, @PathVariable String username) {
        return productService.deleteProduct(id, username);
    }
}