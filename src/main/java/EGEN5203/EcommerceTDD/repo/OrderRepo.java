package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.OrderItem;
import EGEN5203.EcommerceTDD.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    List<Order> findByUser(Users user);

    Order findByIdAndUser(Long orderId, Users user);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems")
    List<Order> findAllWithOrderItems();
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p")
    List<Order> findAllOrdersWithItemsAndProducts();

    // If you need to filter by user
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p " +
            "LEFT JOIN FETCH o.payments pa "+
            "WHERE o.user.id = :userId")
    List<Order> findOrdersByUserWithItemsAndProducts(@Param("userId") Long userId);


}
