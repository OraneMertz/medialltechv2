package com.biblio.medialltechv2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "books")
public class Books {

    @Id
    private String id; // MongoDB utilise des String (ObjectID) au lieu des int
    private String title;
    private String author;
    private String image;
    private boolean isDisponible;
    private String borrow;

    @DBRef
    @JsonBackReference
    private Borrowers borrowBy;

}

