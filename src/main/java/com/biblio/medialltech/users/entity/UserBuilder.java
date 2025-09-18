package com.biblio.medialltech.users.entity;

import com.biblio.medialltech.users.Role;

public class UserBuilder {
    private Long id;
    private String pseudo;
    private String email;
    private String password;
    private Role authorities;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withPseudo(String pseudo) {
        this.pseudo = pseudo;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withAuthorities(Role authorities) {
        this.authorities = authorities;
        return this;
    }

    public UserBuilder withAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
        return this;
    }

    public UserBuilder withAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
        return this;
    }

    public UserBuilder withCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
        return this;
    }

    public User createUser() {
        return new User(id, pseudo, email, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired);
    }
}