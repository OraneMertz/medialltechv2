package com.biblio.medialltech.users.dto;

import com.biblio.medialltech.users.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    private String id;
    private String pseudo;
    private String email;
    private Role authorities;
    private Boolean accountEnable;

    // Mot de passe seulement pour les requêtes entrantes (création/modification)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    public UserDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String username) {
        this.pseudo = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Role authorities) {
        this.authorities = authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAccountEnable() {
        return accountEnable;
    }

    public void setAccountEnable(Boolean accountEnable) {
        this.accountEnable = accountEnable;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                ", accountEnable=" + accountEnable +
                ", password='" + password + '\'' +
                '}';
    }
}