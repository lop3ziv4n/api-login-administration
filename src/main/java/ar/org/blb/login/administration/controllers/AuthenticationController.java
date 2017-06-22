package ar.org.blb.login.administration.controllers;

import ar.org.blb.login.administration.entities.Authentication;
import ar.org.blb.login.administration.entities.User;
import ar.org.blb.login.administration.responses.AuthenticationResponse;
import ar.org.blb.login.administration.responses.HttpStatusResponse;
import ar.org.blb.login.administration.services.AuthenticationService;
import ar.org.blb.login.administration.services.UserService;
import ar.org.blb.login.administration.utilities.LoginUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/authentication/")
public class AuthenticationController {

    private AuthenticationService authenticationService;
    private UserService userService;
    private LoginUtility loginUtility;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService, LoginUtility loginUtility) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.loginUtility = loginUtility;
    }

    @PostMapping(value = "login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) {
        /*AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        Optional<User> userOptional = Optional.of(this.userService.getUserByUsernameOrEmail(user.getUsername(), user.getEmail()));
        if (userOptional.isPresent()) {
            if (userOptional.filter(u -> u.getEnabled()).isPresent()) {
                if (userOptional.filter(u -> this.userService.matchesPassword(u, user.getPassword())).isPresent()) {
                    authenticationResponse = this.createAuthenticationResponse(this.authenticationService.createAuthentication(this.createAuthentication(userOptional.get())));
                } else {
                    authenticationResponse.setStatus(HttpStatusResponse.PASSWORD_INCORRECT);
                }
            } else {
                authenticationResponse.setStatus(HttpStatusResponse.USER_NOT_ACTIVE);
            }
        } else {
            authenticationResponse.setStatus(HttpStatusResponse.USERNAME_NOT_EXISTS);
        }
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);*/

        AuthenticationResponse authenticationResponse =
                Optional.ofNullable(this.userService.getUserByUsernameOrEmail(user.getUsername(), user.getEmail()))
                        .map(u -> Optional.of(u)
                                .filter(userExists -> userExists.getEnabled())
                                .map(userEnabled -> Optional.of(userEnabled)
                                        .filter(userMatchesPassword -> this.userService.matchesPassword(userMatchesPassword, user.getPassword()))
                                        .map(userAuthentication -> this.createAuthenticationResponse(this.authenticationService.createAuthentication(this.createAuthentication(userAuthentication))))
                                        .orElse(new AuthenticationResponse(HttpStatusResponse.PASSWORD_INCORRECT, new Authentication())))
                                .orElse(new AuthenticationResponse(HttpStatusResponse.USER_NOT_ACTIVE, new Authentication())))
                        .orElse(new AuthenticationResponse(HttpStatusResponse.USERNAME_NOT_EXISTS, new Authentication()));

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PutMapping(value = "logout/{user}")
    public ResponseEntity<Void> logout(@PathVariable("id") Long user) {
        return Optional.ofNullable(this.authenticationService.findAuthenticationByUser(user))
                .map(authentication -> {
                    this.authenticationService.deleteAuthentication(authentication.getId());
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "ttl_token/{token}")
    public ResponseEntity<AuthenticationResponse> timeToLiveToken(@PathVariable("token") String token) {
        return new ResponseEntity<>(this.createAuthenticationResponse(this.authenticationService.findAuthenticationByToken(token)), HttpStatus.OK);
    }

    private AuthenticationResponse createAuthenticationResponse(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(a -> new AuthenticationResponse(HttpStatusResponse.AUTHENTICATION_OK, a))
                .orElse(new AuthenticationResponse(HttpStatusResponse.AUTHENTICATION_FAILED, new Authentication()));
    }

    private Authentication createAuthentication(User user) {
        return new Authentication(this.loginUtility.getRandomValue(),
                this.loginUtility.getTokenValidity(),
                this.loginUtility.getCodeRoleCommaSeparated(user.getRoles()),
                user.getId());
    }
}
