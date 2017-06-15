package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.entities.User;
import ar.org.blb.login.administration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return Optional.ofNullable(this.userRepository.findOneByUsername(username))
                .filter(u -> this.passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new RuntimeException("No Exists User"));
    }

    public User createUser(User user) {
        user.setDateCreated(new Date());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setEnabled(Boolean.FALSE);
        return this.userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional.ofNullable(this.userRepository.findOne(id))
                .ifPresent(u -> this.userRepository.delete(u));
    }

    public User updateUser(User user, Long id) {
        return Optional.ofNullable(this.userRepository.findOne(id))
                .map(u -> {
                    u.setDateModify(new Date());
                    u.setPassword(user.getPassword());
                    u.setUsername(user.getUsername());
                    u.setEnabled(user.getEnabled());
                    u.setRoles(user.getRoles());
                    return this.userRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("No Exists User"));
    }
}