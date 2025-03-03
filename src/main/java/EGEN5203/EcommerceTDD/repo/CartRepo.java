package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
}
