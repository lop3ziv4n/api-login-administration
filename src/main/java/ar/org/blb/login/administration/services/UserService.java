package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.entities.User;
import ar.org.blb.login.administration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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

    public Boolean matchesPassword(User user, String password) {
        return this.passwordEncoder.matches(password, user.getPassword());
    }

    public User createUser(User user) {
        user.setDateCreated(new Date());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
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
                    u.setPassword(this.passwordEncoder.encode(user.getPassword()));
                    u.setUsername(user.getUsername());
                    u.setEmail(user.getEmail());
                    u.setEnabled(user.getEnabled());
                    u.setRoles(user.getRoles());
                    return this.userRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("No Exists User"));
    }

    @Value(value = "login.mail.thymeleaf.reset-password.mail-template-nam")
    private String template;

    public void sendMailResetPassword(User user) {
        Map<String, String> message = new HashMap<>();
        message.put("password", user.getPassword());
        message.put("date", new Date().toString());
        message.put("description", "Change Password User");
        message.put("user", user.getId().toString());

        this.emailService.sendMail(user.getEmail(), "Change Password", template, message, Boolean.TRUE, Boolean.TRUE);
    }
}