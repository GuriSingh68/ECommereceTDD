package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {

    /**
     * Find a cart item by user and product
     * @return Optional containing the cart item if found, empty otherwise
     */
    @Modifying
    @Query("UPDATE Cart c SET c.quantity = :quantity, c.price = :price WHERE c.id = :id")
    void updateCartItemQuantityAndPrice(@Param("id") Long id, @Param("quantity") int quantity, @Param("price") double price);
}
