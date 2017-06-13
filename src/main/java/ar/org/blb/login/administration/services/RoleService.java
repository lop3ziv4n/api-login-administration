package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.entities.Role;
import ar.org.blb.login.administration.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRole() {
        return this.roleRepository.findAll();
    }

    public Role getRoleByCode(String code) {
        return this.roleRepository.findOneByCode(code);
    }
}
