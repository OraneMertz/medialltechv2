package com.biblio.medialltechv2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class Users {

    @Id
    private String id;
    private String username;
    @JsonIgnore
    private String password;
    private boolean passwordChanged;
    private String firstName;
    private String lastName;
    private String email;
    private List<Roles> roles;

    public Users(String username, String password, String firstName, String lastName, String email, List<Roles> roles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }
}
