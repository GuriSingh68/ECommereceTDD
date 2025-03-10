package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products") // Base URL for product-related endpoints
public class ProductController {

    @Autowired
    public ProductService productService;
    @GetMapping("/productlist")
    public String viewAllProducts(){
        return productService.fetchAllProducts().toString();
    }
    @PostMapping("/add/{username}")
    public String addProducts(@PathVariable String username, @RequestBody AddProductsDto addProductsDTO) {
        return productService.addProducts(username, addProductsDTO);
    }

    @PutMapping("/update/{id}/{username}")
    public String updateProduct(@PathVariable Long id, @PathVariable String username, @RequestBody AddProductsDto updateProductsDTO) {
        return productService.updateProduct(id, username, updateProductsDTO);
    }

    @DeleteMapping("/delete/{id}/{username}")
    public String deleteProduct(@PathVariable Long id, @PathVariable String username) {
        return productService.deleteProduct(id, username);
    }

}