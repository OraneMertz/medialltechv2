package com.biblio.medialltech.dto;

import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.entity.User;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String image;
    private boolean isDisponible;
    private User borrower;
    private Category category;

    public BookDTO() {}

    public BookDTO(Long id, String title, String author, String image, boolean isDisponible,User borrower, Category category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.isDisponible = isDisponible;
        this.borrower = borrower;
        this.category = category;
    }

    public BookDTO(Long id, String title, String author, String image, boolean isDisponible, User borrower) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.isDisponible = isDisponible;
        this.borrower = borrower;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isDisponible() {
        return isDisponible;
    }

    public void setDisponible(boolean disponible) {
        isDisponible = disponible;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }
}
