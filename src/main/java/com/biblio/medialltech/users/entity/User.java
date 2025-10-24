package com.biblio.medialltech.users.entity;

import com.biblio.medialltech.users.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Field("pseudo")
    @Indexed(unique = true)
    private String pseudo;

    @Field("email")
    @Indexed(unique = true)
    private String email;

    @Field("password")
    private String password;

    @Field("authorities")
    private Role authorities;

    @Field("account_enable")
    private Boolean accountEnable;

    public User() {
    }

    public User(String id, String pseudo, String email, String password, Role authorities, Boolean accountEnable) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.accountEnable = accountEnable;
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

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Role role) {
        this.authorities = role;
    }

    public Boolean getAccountEnable() {
        return accountEnable;
    }

    public void setAccountEnable(Boolean accountEnable) {
        this.accountEnable = accountEnable;
    }
}