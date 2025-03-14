package com.biblio.medialltechv2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "borrowers")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Borrowers extends Users {

    private boolean isActive;

    @JsonManagedReference
    private List<Books> borrowedBooks = new ArrayList<>();
}

