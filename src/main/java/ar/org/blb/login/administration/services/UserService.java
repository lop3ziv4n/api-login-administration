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

    public User getUserById(Long id) {
        return this.userRepository.findOne(id);
    }

    public User getUserByUsernameOrEmail(String username, String email) {
        return this.userRepository.findOneByUsernameOrEmail(username, email);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    public Boolean matchesPassword(String rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User createUser(User user) {
        user.setDateCreated(new Date());
        user.setEnabled(Boolean.FALSE);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional.ofNullable(this.userRepository.findOne(id))
                .ifPresent(u -> this.userRepository.delete(u));
    }

    public User updateUserStatus(Boolean enabled, Long id) {
        return Optional.ofNullable(this.userRepository.findOne(id))
                .map(u -> {
                    u.setDateModify(new Date());
                    u.setEnabled(enabled);
                    return this.userRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("No Exists User"));
    }

    public User updateUserPassword(String password, Long id) {
        return Optional.ofNullable(this.userRepository.findOne(id))
                .map(u -> {
                    u.setDateModify(new Date());
                    u.setPassword(this.passwordEncoder.encode(password));
                    return this.userRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("No Exists User"));
    }
}