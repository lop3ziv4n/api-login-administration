package ar.org.blb.login.administration.controllers;

import ar.org.blb.login.administration.entities.Activation;
import ar.org.blb.login.administration.entities.User;
import ar.org.blb.login.administration.responses.HttpStatusResponse;
import ar.org.blb.login.administration.responses.UserResponse;
import ar.org.blb.login.administration.services.ActivationService;
import ar.org.blb.login.administration.services.UserService;
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
    private LoginUtility loginUtility;

    @Autowired
    public UserController(UserService userService, ActivationService activationService, LoginUtility loginUtility) {
        this.userService = userService;
        this.activationService = activationService;
        this.loginUtility = loginUtility;
    }

    @PostMapping(value = "create")
    public ResponseEntity<UserResponse> create(@RequestBody User user) {

        if (Optional.ofNullable(this.userService.getUserByUsernameOrEmail(user.getUsername(), user.getEmail())).isPresent()) {
            return new ResponseEntity<>(new UserResponse(HttpStatusResponse.USERNAME_EXISTS, new User()), HttpStatus.OK);
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

        UserResponse userResponse = Optional.ofNullable(this.userService.createUser(user))
                .map(u -> Optional.ofNullable(this.activationService.createActivation(this.createActivation(u)))
                        .map(a -> {
                            this.activationService.sendMailActivation(a);
                            return new UserResponse(HttpStatusResponse.ACTIVATION_NOTIFICATION, u);
                        })
                        .orElse(new UserResponse(HttpStatusResponse.ACTIVATION_NOTIFICATION_FAILED, new User())))
                .orElse(new UserResponse(HttpStatusResponse.USER_CREATED_FAILED, new User()));

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    private Activation createActivation(User user) {
        return new Activation(user.getEmail(),
                this.loginUtility.getRandomValue(),
                this.loginUtility.getExpirationDate(),
                Boolean.FALSE,
                user.getId());
    }

    @PutMapping(value = "notification-reset-password/{email}")
    public ResponseEntity<UserResponse> notificationResetPassword(@PathVariable("email") String email) {
        UserResponse userResponse = Optional.ofNullable(this.userService.getUserByEmail(email))
                .map(user -> Optional.of(user)
                        .filter(userExists -> userExists.getEnabled())
                        .map(userEnabled -> Optional.of(userEnabled)
                                .map(userResetPassword -> {
                                    this.userService.sendMailResetPassword(userResetPassword);
                                    return new UserResponse(HttpStatusResponse.RESET_PASSWORD_NOTIFICATION, userResetPassword);
                                })
                                .orElse(new UserResponse(HttpStatusResponse.RESET_PASSWORD_NOTIFICATION_FAILED, new User())))
                        .orElse(new UserResponse(HttpStatusResponse.USER_NOT_ACTIVE, new User())))
                .orElse(new UserResponse(HttpStatusResponse.USERNAME_NOT_EXISTS, new User()));

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping(value = "change-password/{id}")
    public ResponseEntity<UserResponse> changePassword(@PathVariable("id") Long id, @RequestBody String password) {
        UserResponse userResponse = Optional.ofNullable(this.userService.getUserById(id))
                .map(user -> Optional.of(user)
                        .filter(userExists -> userExists.getEnabled())
                        .map(userEnabled -> Optional.of(userEnabled)
                                .map(userChangePassword -> {
                                    userChangePassword.setPassword(password);
                                    this.userService.updateUser(userChangePassword, id);
                                    return new UserResponse(HttpStatusResponse.PASSWORD_CHANGED, userChangePassword);
                                })
                                .orElse(new UserResponse(HttpStatusResponse.PASSWORD_CHANGED_FAILED, new User())))
                        .orElse(new UserResponse(HttpStatusResponse.USER_NOT_ACTIVE, new User())))
                .orElse(new UserResponse(HttpStatusResponse.USERNAME_NOT_EXISTS, new User()));

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}

