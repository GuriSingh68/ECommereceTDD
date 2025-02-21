package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users,Integer> {

    boolean existsByEmail(String email);

    Users findByEmail(String email);
}
