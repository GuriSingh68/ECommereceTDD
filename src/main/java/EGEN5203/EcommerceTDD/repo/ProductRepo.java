package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    Product findByProductName(String productName);
}
