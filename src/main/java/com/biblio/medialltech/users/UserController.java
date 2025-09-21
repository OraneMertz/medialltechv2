package com.biblio.medialltech.users;

import com.biblio.medialltech.logs.ServiceResponse;
import com.biblio.medialltech.users.dto.ChangePasswordDTO;
import com.biblio.medialltech.users.dto.UserDTO;
import com.biblio.medialltech.users.service.UserService;
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
    public ResponseEntity<ServiceResponse<UserDTO>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<UserDTO>> updateUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<Boolean>> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ServiceResponse<Boolean>> changePassword(
            @PathVariable String id,
            @RequestBody ChangePasswordDTO request) {
        return ResponseEntity.ok(userService.changePassword(id, request));
    }
}