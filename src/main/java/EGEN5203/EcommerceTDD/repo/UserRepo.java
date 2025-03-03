package EGEN5203.EcommerceTDD.repo;

import EGEN5203.EcommerceTDD.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {

    boolean existsByEmail(String email);

    Users findByEmail(String email);
}
