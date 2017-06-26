package ar.org.blb.login.administration.controllers;

import ar.org.blb.login.administration.entities.Activation;
import ar.org.blb.login.administration.entities.Authentication;
import ar.org.blb.login.administration.entities.ResetPassword;
import ar.org.blb.login.administration.entities.User;
import ar.org.blb.login.administration.responses.AuthenticationResponse;
import ar.org.blb.login.administration.services.ActivationService;
import ar.org.blb.login.administration.services.ResetPasswordService;
import ar.org.blb.login.administration.services.UserService;
import ar.org.blb.login.administration.utilities.AuthenticationStatus;
import ar.org.blb.login.administration.utilities.LoginUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/user/")
public class UserController {

    private UserService userService;
    private ActivationService activationService;
    private ResetPasswordService resetPasswordService;
    private LoginUtility loginUtility;

    @Autowired
    public UserController(UserService userService, ActivationService activationService, ResetPasswordService resetPasswordService, LoginUtility loginUtility) {
        this.userService = userService;
        this.activationService = activationService;
        this.resetPasswordService = resetPasswordService;
        this.loginUtility = loginUtility;
    }

    @PostMapping(value = "create")
    public ResponseEntity<AuthenticationResponse> create(@RequestBody User user) {

        if (Optional.ofNullable(this.userService.getUserByUsernameOrEmail(user.getUsername(), user.getEmail())).isPresent()) {
            return new ResponseEntity<>(new AuthenticationResponse(AuthenticationStatus.USERNAME_EXISTS, new Authentication()), HttpStatus.OK);
        }

        /*UserResponse userResponse = new UserResponse();
        Optional<User> userOptional = Optional.ofNullable(this.userService.createUser(user));
        if (userOptional.isPresent()) {
            Optional<Activation> activationOptional = Optional.ofNullable(this.activationService.createActivation(this.createActivation(userOptional.get())))
                    .map(a -> this.activationService.sendMailActivation(a));
            if (activationOptional.isPresent()){
                userResponse.setEntity(activationOptional.get());
                userResponse.setStatus(UserStatus.ACTIVATION_NOTIFICATION_FAILED);
            } else {
                userResponse.setStatus(UserStatus.ACTIVATION_NOTIFICATION);
            }
        } else {
            userResponse.setStatus(UserStatus.USER_CREATED_FAILED);
        }
        return new ResponseEntity<>(userResponse, HttpStatus.OK);*/

        AuthenticationResponse authenticationResponse = Optional.ofNullable(this.userService.createUser(user))
                .map(userCreated -> Optional.ofNullable(this.activationService.createActivation(this.createActivation(userCreated)))
                        .map(activation -> {
                            this.activationService.sendMailActivation(activation);
                            return new AuthenticationResponse(AuthenticationStatus.USER_CREATED, new Authentication());
                        })
                        .orElse(new AuthenticationResponse(AuthenticationStatus.ACTIVATION_NOTIFICATION_FAILED, new Authentication())))
                .orElse(new AuthenticationResponse(AuthenticationStatus.USER_CREATED_FAILED, new Authentication()));

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    private Activation createActivation(User user) {
        return new Activation(user.getEmail(),
                this.loginUtility.getRandomValue(),
                this.loginUtility.getExpirationDate(),
                Boolean.FALSE,
                user.getId());
    }

    @PutMapping(value = "reset-password", params = "email")
    public ResponseEntity<AuthenticationResponse> resetPassword(@RequestParam("email") String email) {
        AuthenticationResponse authenticationResponse = Optional.ofNullable(this.userService.getUserByEmail(email))
                .map(user -> Optional.of(user)
                        .filter(userExists -> user.getEnabled())
                        .map(userEnabled -> Optional.ofNullable(this.resetPasswordService.createResetPassword(this.createResetPassword(user)))
                                .map(resetPassword -> {
                                    this.resetPasswordService.sendMailResetPassword(resetPassword);
                                    return new AuthenticationResponse(AuthenticationStatus.RESET_PASSWORD_NOTIFICATION, new Authentication());
                                })
                                .orElse(new AuthenticationResponse(AuthenticationStatus.RESET_PASSWORD_NOTIFICATION_FAILED, new Authentication())))
                        .orElse(new AuthenticationResponse(AuthenticationStatus.USER_NOT_ACTIVE, new Authentication())))
                .orElse(new AuthenticationResponse(AuthenticationStatus.EMAIL_NOT_EXISTS, new Authentication()));

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    private ResetPassword createResetPassword(User user) {
        return new ResetPassword(user.getEmail(),
                this.loginUtility.getRandomValue(),
                user.getId());
    }

    @PutMapping(value = "change-password/{key}")
    public ResponseEntity<AuthenticationResponse> changePassword(@PathVariable("key") String key, @RequestBody String password) {
        AuthenticationResponse authenticationResponse = Optional.ofNullable(this.resetPasswordService.findResetPasswordByKey(key))
                .map(resetPassword -> Optional.ofNullable(this.userService.getUserById(resetPassword.getUser()))
                        .map(user -> Optional.ofNullable(user)
                                .filter(userExists -> user.getEnabled())
                                .map(userEnabled -> Optional.of(user)
                                        .map(userChangePassword -> {
                                            user.setPassword(password);
                                            this.userService.updateUser(user, user.getId());
                                            this.resetPasswordService.deleteResetPassword(resetPassword.getId());
                                            return new AuthenticationResponse(AuthenticationStatus.PASSWORD_CHANGED, new Authentication());
                                        })
                                        .orElse(new AuthenticationResponse(AuthenticationStatus.PASSWORD_CHANGED_FAILED, new Authentication())))
                                .orElse(new AuthenticationResponse(AuthenticationStatus.USER_NOT_ACTIVE, new Authentication())))
                        .orElse(new AuthenticationResponse(AuthenticationStatus.USER_NOT_EXISTS, new Authentication())))
                .orElse(new AuthenticationResponse(AuthenticationStatus.RESET_PASSWORD_KEY_NOT_EXISTS, new Authentication()));

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PutMapping(value = "activation/{key}")
    public ResponseEntity<AuthenticationResponse> activation(@PathVariable("key") String key) {
        AuthenticationResponse authenticationResponse = Optional.ofNullable(this.activationService.findActivationByKey(key))
                .map(activation -> Optional.ofNullable(this.userService.getUserById(activation.getUser()))
                        .map(user -> {
                            user.setEnabled(Boolean.TRUE);
                            this.userService.updateUser(user, user.getId());
                            this.activationService.deleteActivation(activation.getId());
                            return new AuthenticationResponse(AuthenticationStatus.ACTIVATION_OK, new Authentication());
                        })
                        .orElse(new AuthenticationResponse(AuthenticationStatus.USER_NOT_EXISTS, new Authentication())))
                .orElse(new AuthenticationResponse(AuthenticationStatus.ACTIVATION_FAILED, new Authentication()));

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }
}

