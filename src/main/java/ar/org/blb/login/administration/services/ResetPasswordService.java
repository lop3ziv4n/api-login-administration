package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.entities.ResetPassword;
import ar.org.blb.login.administration.repositories.ResetPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ResetPasswordService {

    private ResetPasswordRepository resetPasswordRepository;
    private EmailService emailService;

    @Autowired
    public ResetPasswordService(ResetPasswordRepository resetPasswordRepository, EmailService emailService) {
        this.resetPasswordRepository = resetPasswordRepository;
        this.emailService = emailService;
    }

    public ResetPassword findResetPasswordByKey(String key) {
        return this.resetPasswordRepository.findOneByKey(key);
    }

    public ResetPassword createResetPassword(ResetPassword resetPassword) {
        return this.resetPasswordRepository.save(resetPassword);
    }

    public void deleteResetPassword(Long id) {
        Optional.ofNullable(this.resetPasswordRepository.findOne(id))
                .ifPresent(r -> this.resetPasswordRepository.delete(r));
    }

    public ResetPassword updateResetPassword(ResetPassword resetPassword, Long id) {
        return Optional.ofNullable(this.resetPasswordRepository.findOne(id))
                .map(r -> {
                    r.setKey(resetPassword.getKey());
                    r.setEmail(resetPassword.getEmail());
                    r.setUser(resetPassword.getUser());
                    return this.resetPasswordRepository.save(r);
                })
                .orElseThrow(() -> new RuntimeException("No Exists Reset Password"));
    }

    @Value("${login.mail.thymeleaf.reset-password.mail-template-name}")
    private String template;

    public void sendMailResetPassword(ResetPassword resetPassword) {
        Map<String, String> message = new HashMap<>();
        message.put("key", resetPassword.getKey());
        message.put("date", new Date().toString());
        message.put("description", "Change Password User");
        message.put("user", resetPassword.getUser().toString());

        this.emailService.sendMail(resetPassword.getEmail(), "Change Password", template, message, Boolean.TRUE, Boolean.TRUE);
    }
}
