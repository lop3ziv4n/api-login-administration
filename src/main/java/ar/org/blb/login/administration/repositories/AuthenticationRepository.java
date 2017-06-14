package ar.org.blb.login.administration.repositories;

import ar.org.blb.login.administration.entities.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {

    Authentication findOneByToken(String token);

    Authentication findOneByUser(Long user);
}
