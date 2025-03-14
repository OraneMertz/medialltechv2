package com.biblio.medialltechv2.controller;

import com.biblio.medialltechv2.dto.ChangePasswordRequest;
import com.biblio.medialltechv2.model.Users;
import com.biblio.medialltechv2.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        Users user = usersService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody Users user) {
        usersService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur créé avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody Users userDetails) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = usersService.getUserByUsername(currentUsername);

        if (!currentUser.getId().equals(id) && !currentUser.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vous n'êtes pas autorisé à modifier cet utilisateur.");
        }

        userDetails.setId(id);  // Ensure the user ID is not changed
        usersService.updateUser(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("Utilisateur modifié avec succès.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        usersService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Utilisateur supprimé avec succès.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersService.getUserByUsername(currentUsername);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Le mot de passe est incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChanged(true);
        usersService.updateUser(user);

        return ResponseEntity.ok("Mot de passe modifié avec succès.");
    }
}
