package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddProductsDto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private UserRepo userRepo;

    public String addProducts(String userid, AddProductsDto addProductsDTO) {
        Users user=userRepo.findByEmail(userid);
        if (user.getRole().equals(Roles.ADMIN)){
            Product product = new Product();
            product.setProductName(addProductsDTO.getProductName());
            product.setPrice(addProductsDTO.getPrice());
            product.setQuantity(addProductsDTO.getQuantity());
            productRepo.save(product);
            return "Product added successfully!";
        }
        else {
            throw new IllegalArgumentException("Only admin can add products");
        }
    }
}
