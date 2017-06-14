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

    @RequestMapping(method = RequestMethod.GET, value = "exist/", params = "username")
    public ResponseEntity<Boolean> existByUsername(@RequestParam("username") String username) {
        return Optional.ofNullable(this.userService.isUserExistByUsername(username))
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @RequestMapping(method = RequestMethod.POST, value = "username/", params = "username")
    public ResponseEntity<User> getByUsernameAndPassword(@RequestParam("username") String username, @RequestBody String password) {
        return Optional.ofNullable(this.userService.getUserByUsernameAndPassword(username, password))
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

