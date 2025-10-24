package com.biblio.medialltech.users.entity;

import com.biblio.medialltech.users.Role;

public class UserBuilder {
    private String id;
    private String pseudo;
    private String email;
    private String password;
    private Role authorities;
    private Boolean accountEnable;

    public UserBuilder withId(String id) {
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

    public UserBuilder withAccountEnable(Boolean accountEnable) {
        this.accountEnable = accountEnable;
        return this;
    }

    public User createUser() {
        return new User(id, pseudo, email, password, authorities, accountEnable);
    }
}