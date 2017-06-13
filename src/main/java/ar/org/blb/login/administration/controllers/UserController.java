package ar.org.blb.login.administration.controllers;

import ar.org.blb.login.administration.entities.User;
import ar.org.blb.login.administration.services.UserService;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "exist/", params = "login")
    public ResponseEntity<Boolean> existByLogin(@RequestParam("login") String login) {
        return Optional.ofNullable(this.userService.isUserExistByLogin(login))
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @RequestMapping(method = RequestMethod.POST, value = "login/", params = "login")
    public ResponseEntity<User> getByLoginAndPassword(@RequestParam("login") String login, @RequestBody String password) {
        return Optional.ofNullable(this.userService.getUserByLoginAndPassword(login, password))
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody User user) {
        return Optional.ofNullable(this.userService.createUser(user))
                .map(a -> new ResponseEntity<>(a, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<User> updatePassword(@PathVariable("id") Long id, @RequestBody String password) {
        return Optional.ofNullable(this.userService.updateUserPassword(id, password))
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
    }
}

