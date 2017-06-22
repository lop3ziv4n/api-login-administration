package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.entities.Activation;
import ar.org.blb.login.administration.repositories.ActivationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivationService {

    private ActivationRepository activationRepository;
    private EmailService emailService;

    @Autowired
    public ActivationService(ActivationRepository activationRepository, EmailService emailService) {
        this.activationRepository = activationRepository;
        this.emailService = emailService;
    }

    public Activation findActivationByUser(Long user) {
        return this.activationRepository.findOneByUser(user);
    }

    public Activation findActivationByKey(String key) {
        return this.activationRepository.findOneByKey(key);
    }

    public List<Activation> findAllActivationByDateExpiryLessThan(Date dateExpiry) {
        return this.activationRepository.findAllByDateExpiryLessThan(dateExpiry);
    }

    public List<Activation> findAllActivationByNotificationFalse() {
        return this.activationRepository.findAllByNotificationFalse();
    }

    public Activation createActivation(Activation activation) {
        return this.activationRepository.save(activation);
    }

    public void deleteActivation(Long id) {
        Optional.ofNullable(this.activationRepository.findOne(id))
                .ifPresent(a -> this.activationRepository.delete(a));
    }

    public Activation updateActivation(Activation activation, Long id) {
        return Optional.ofNullable(this.activationRepository.findOne(id))
                .map(a -> {
                    a.setKey(activation.getKey());
                    a.setEmail(activation.getEmail());
                    a.setDateExpiry(activation.getDateExpiry());
                    a.setNotification(activation.getNotification());
                    a.setUser(activation.getUser());
                    return this.activationRepository.save(a);
                })
                .orElseThrow(() -> new RuntimeException("No Exists Activation"));
    }

    @Value(value = "login.mail.thymeleaf.activation.mail-template-name")
    private String template;

    public Activation sendMailActivation(Activation activation) {
        Map<String, String> message = new HashMap<>();
        message.put("key", activation.getKey());
        message.put("date", new Date().toString());
        message.put("description", "Activate User");
        message.put("user", activation.getUser().toString());

        this.emailService.sendMail(activation.getEmail(), "Activate User", template, message, Boolean.TRUE, Boolean.TRUE);

        activation.setNotification(Boolean.TRUE);
        return this.activationRepository.save(activation);
    }
}
