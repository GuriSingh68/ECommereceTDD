package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Cart;
import EGEN5203.EcommerceTDD.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
}

