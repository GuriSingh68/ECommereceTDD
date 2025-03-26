package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payments, Integer> {

}
