package com.biblio.medialltech.users;

import com.biblio.medialltech.logs.ServiceResponse;
import com.biblio.medialltech.security.ChangePasswordDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ServiceResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping
    public ResponseEntity<ServiceResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<UserDTO>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ServiceResponse<UserDTO>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ServiceResponse<UserDTO>> login(
            @RequestParam String username,
            @RequestParam String password) {
        return ResponseEntity.ok(userService.authenticateUser(username, password));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<Boolean>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ServiceResponse<Boolean>> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordDTO request) {
        return ResponseEntity.ok(userService.changePassword(id, request));
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<ServiceResponse<Boolean>> isUsernameExists(@PathVariable String username) {
        return ResponseEntity.ok(userService.isUsernameExists(username));
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<ServiceResponse<Boolean>> isEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(userService.isEmailExists(email));
    }
}