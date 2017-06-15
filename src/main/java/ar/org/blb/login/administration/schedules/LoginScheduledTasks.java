package ar.org.blb.login.administration.schedules;

import ar.org.blb.login.administration.entities.Activation;
import ar.org.blb.login.administration.services.ActivationService;
import ar.org.blb.login.administration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class LoginScheduledTasks {

    private ActivationService activationService;
    private UserService userService;

    @Autowired
    private LoginScheduledTasks(ActivationService activationService, UserService userService) {
        this.activationService = activationService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<Activation> activations = this.activationService.findAllActivationByDateExpiryLessThan(new Date());
        for (Activation activation : activations) {
            this.activationService.deleteActivation(activation.getId());
            this.userService.deleteUser(activation.getUser());
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void sendMailActivationNotNotifiedUsers() {
        List<Activation> activations = this.activationService.findAllActivationByNotificationFalse();
        for (Activation activation : activations) {
            this.activationService.sendMailActivation(activation);
        }
    }
}
