package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.entities.User;
import ar.org.blb.login.administration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean isUserExistByUsername(String username) {
        return Optional.ofNullable(this.userRepository.findOneByUsername(username))
                .map(u -> Boolean.TRUE)
                .orElse(Boolean.FALSE);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return Optional.ofNullable(this.userRepository.findOneByUsername(username))
                .filter(u -> this.passwordEncoder().matches(password, u.getPassword()))
                .map(e -> e)
                .orElseThrow(() -> new RuntimeException("No Exists User"));
    }

    public User createUser(User user) {
        user.setDateCreated(new Date());
        user.setPassword(this.passwordEncoder().encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional.ofNullable(this.userRepository.findOne(id))
                .ifPresent(u -> this.userRepository.delete(u));
    }

    public User updateUserPassword(Long id, String password) {
        return Optional.ofNullable(this.userRepository.findOne(id))
                .map(u -> {
                    u.setDateModify(new Date());
                    u.setPassword(password);
                    return this.userRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("No Exists User"));
    }

    public User updateUserEnabled(Long id, Boolean enabled) {
        return Optional.ofNullable(this.userRepository.findOne(id))
                .map(u -> {
                    u.setDateModify(new Date());
                    u.setEnabled(enabled);
                    return this.userRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("No Exists User"));
    }
}
