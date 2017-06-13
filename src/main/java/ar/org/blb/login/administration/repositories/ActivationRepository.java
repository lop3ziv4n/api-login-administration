package ar.org.blb.login.administration.repositories;

import ar.org.blb.login.administration.entities.Activation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActivationRepository extends JpaRepository<Activation, Long> {

    Activation findOneByToken(String token);

    Activation findOneByUser(Long user);

    List<Activation> findAllByDateExpiryLessThan(Date date);

    List<Activation> findAllByNotificationFalse();
}
