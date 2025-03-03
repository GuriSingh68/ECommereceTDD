package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    @Autowired
    public ProductService productService;
    @PostMapping("/addProducts/{username}")
    public String addProducts(@PathVariable String username, @RequestBody AddProductsDto addProductsDTO){
        return productService.addProducts(username,addProductsDTO);
    }
}
