package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing product data.
 * Extends JpaRepository to provide CRUD operations for Product entities.
 */
@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    /**
     * Finds a product by its name.
     *
     * @param name the name of the product.
     * @return the found Product, or null if not found.
     */
    Product findByName(String name);

    /**
     * Checks if a product exists by its name.
     *
     * @param name the name of the product.
     * @return true if the product exists, false otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.name = :name") // Updated to match the new field name
    Boolean existsByName(@Param("name") String name); // Updated to match the new field name

    /**
     * Decreases the stock quantity of a product.
     *
     * @param id the ID of the product.
     * @param amount the amount to decrease.
     * @return the number of affected rows.
     */
    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :amount WHERE p.productId = :id AND p.stock >= :amount") // Updated to match the new field name
    int decreaseProductQuantity(@Param("id") Long id, @Param("amount") int amount);

    /**
     * Finds a product by its ID.
     *
     * @param id the ID of the product.
     * @return the found Product, or null if not found.
     */
    Product findByProductId(Long id);
}