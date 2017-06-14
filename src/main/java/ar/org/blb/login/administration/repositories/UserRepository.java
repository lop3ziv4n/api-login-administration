package ar.org.blb.login.administration.repositories;

import ar.org.blb.login.administration.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByUsername(String username);
}
