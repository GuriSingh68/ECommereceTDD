package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.user = :user")
    List<Order> findByUserWithItems(@Param("user") Users user);
    Order findByIdAndUser (Long orderId, Users user);
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi JOIN oi.product p WHERE p.users.user_id = :adminId")
    List<Order> findOrdersByAdminId(@Param("adminId") Long adminId);
    List<Order> findByUser(Users user);
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p")
    List<Order> findAllOrdersWithItemsAndProducts();
}