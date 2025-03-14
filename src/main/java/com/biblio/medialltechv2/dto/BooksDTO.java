package com.biblio.medialltechv2.dto;

import lombok.Data;

@Data
public class BooksDTO {

    private String id;
    private String title;
    private String author;
    private String image;
    private boolean isDisponible;
    private String borrow;
    private int borrowBy;

    public BooksDTO(String id, String title, String author, String image, boolean isDisponible, String borrow, int borrowBy) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.isDisponible = isDisponible;
        this.borrow = borrow;
        this.borrowBy = borrowBy;
    }
}
