package ar.org.blb.login.administration.controllers;

import ar.org.blb.login.administration.entities.Activation;
import ar.org.blb.login.administration.services.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/activation/")
public class ActivationController {

    private ActivationService activationService;

    @Autowired
    public ActivationController(ActivationService activationService) {
        this.activationService = activationService;
    }


    @RequestMapping(method = RequestMethod.GET, value = "user/{user}")
    public ResponseEntity<Activation> getByUser(@PathVariable("user") Long user) {
        return Optional.ofNullable(this.activationService.findActivationByUser(user))
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @RequestMapping(method = RequestMethod.POST, value = "{user}")
    public ResponseEntity<Activation> create(@PathVariable("user") Long user, @RequestBody String email) {
        return Optional.ofNullable(this.activationService.createActivationByUser(user, email))
                .map(a -> new ResponseEntity<>(a, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @RequestMapping(method = RequestMethod.POST, value = "send_mail/{user}")
    public ResponseEntity<Void> sendMail(@PathVariable("user") Long user) {
        this.activationService.sendMailActivationByUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<Activation> updateEmail(@PathVariable("id") Long id, @RequestBody String email) {
        return Optional.ofNullable(this.activationService.updateActivationEmail(id, email))
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{token}")
    public ResponseEntity<Void> delete(@PathVariable("token") String token) {
        this.activationService.deleteActivationByUserActivate(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
