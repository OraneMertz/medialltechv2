package com.biblio.medialltech.dto;

import com.biblio.medialltech.entity.Role;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private String role;

    public UserDTO(Long id, String username, String fullname, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.role = roles.stream().findFirst().map(Role::getName).orElse("Aucun rôle");
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
