package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.entities.Activation;
import ar.org.blb.login.administration.repositories.ActivationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivationService {

    private ActivationRepository activationRepository;

    private UserService userService;
    private EmailService emailService;

    @Autowired
    public ActivationService(ActivationRepository activationRepository, UserService userService, EmailService emailService) {
        this.activationRepository = activationRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    public Activation findActivationByUser(Long user) {
        return this.activationRepository.findOneByUser(user);
    }

    public Activation createActivationByUser(Long user, String email) {
        this.userService.updateUserEnabled(user, Boolean.FALSE);
        return this.activationRepository.save(new Activation(email, this.randomToken(), this.expirationDate(), Boolean.FALSE, user));
    }

    public void deleteActivationByUserActivate(String token) {
        Optional.ofNullable(this.activationRepository.findOneByToken(token))
                .map(a -> {
                    this.activationRepository.delete(a);
                    return this.userService.updateUserEnabled(a.getUser(), Boolean.TRUE);
                })
                .orElseThrow(() -> new RuntimeException("No Exist Activation"));
    }

    public Activation updateActivationEmail(Long id, String email) {
        return Optional.ofNullable(this.activationRepository.findOne(id))
                .map(a -> {
                    a.setEmail(email);
                    return this.activationRepository.save(a);
                })
                .orElseThrow(() -> new RuntimeException("No Exists Activation"));
    }

    public void sendMailActivationByUser(Long user) {
        Optional.ofNullable(this.activationRepository.findOneByUser(user))
                .map(a -> {
                    this.sendMail(a);
                    a.setNotification(Boolean.TRUE);
                    return this.activationRepository.save(a);
                })
                .orElseThrow(() -> new RuntimeException("No Exists Activation"));
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<Activation> activations = this.activationRepository.findAllByDateExpiryLessThan(new Date());
        for (Activation activation : activations) {
            this.activationRepository.delete(activation.getId());
            this.userService.deleteUser(activation.getUser());
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void sendMailNotNotifiedUsers() {
        List<Activation> activations = this.activationRepository.findAllByNotificationFalse();
        for (Activation activation : activations) {
            this.sendMail(activation);
            activation.setNotification(Boolean.TRUE);
            this.activationRepository.save(activation);
        }
    }

    private static final String TOKEN = "token";
    private static final String USER = "user";
    private static final String DESCRIPTION = "description";
    private static final String DATE = "date";
    private static final String MAIL_TEMPLATE_NAME = "template-mail.html";

    private void sendMail(Activation activation) {
        Map<String, String> message = new HashMap<>();
        message.put(TOKEN, activation.getToken());
        message.put(DATE, new Date().toString());
        message.put(DESCRIPTION, "Activate User");
        message.put(USER, activation.getUser().toString());

        this.emailService.sendMail(activation.getEmail(), "Mail Example", MAIL_TEMPLATE_NAME, message, Boolean.TRUE, Boolean.TRUE);
    }

    private Date expirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 3);
        return calendar.getTime();
    }

    private String randomToken() {
        return UUID.randomUUID().toString();
    }
}
