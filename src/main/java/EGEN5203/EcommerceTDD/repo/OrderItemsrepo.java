package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsrepo extends JpaRepository<OrderItem,Integer> {

}
