package com.biblio.medialltech.dto;

import com.biblio.medialltech.entity.Role;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private String role;
    private String password;


    public UserDTO() {
    }

    public UserDTO(Long id, String username, String fullname, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.role = roles.stream().findFirst().map(Role::getName).orElse("Aucun rôle");
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}