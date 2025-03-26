package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.OrderItem;
import EGEN5203.EcommerceTDD.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    List<Order> findByUser(Users user);

    Order findByIdAndUser(Long orderId, Users user);
}
