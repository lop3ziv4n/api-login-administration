package ar.org.blb.login.administration.repositories;

import ar.org.blb.login.administration.entities.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {

    ResetPassword findOneByKey(String key);
}
