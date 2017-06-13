package ar.org.blb.login.administration.repositories;

import ar.org.blb.login.administration.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findOneByCode(String code);
}
