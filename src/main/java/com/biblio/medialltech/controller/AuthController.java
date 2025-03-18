package com.biblio.medialltech.controller;

import com.biblio.medialltech.model.LoginRequest;
import com.biblio.medialltech.model.User;
import com.biblio.medialltech.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.getUserByUsername(loginRequest.getUsername());
        if (user.isPresent() && userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            User responseUser = user.get();
            responseUser.setPassword(null); // NE JAMAIS RENVOYER LE MOT DE PASSE
            return new ResponseEntity<>(responseUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
