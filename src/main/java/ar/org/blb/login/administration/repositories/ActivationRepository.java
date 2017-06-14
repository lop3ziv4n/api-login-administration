package ar.org.blb.login.administration.repositories;

import ar.org.blb.login.administration.entities.Activation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActivationRepository extends JpaRepository<Activation, Long> {

    Activation findOneByKey(String key);

    Activation findOneByUser(Long user);

    List<Activation> findAllByDateExpiryLessThan(Date dateExpiry);

    List<Activation> findAllByNotificationFalse();
}
