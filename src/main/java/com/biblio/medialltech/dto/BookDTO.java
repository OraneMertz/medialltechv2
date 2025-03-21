package com.biblio.medialltech.dto;

import com.biblio.medialltech.entity.BookStatus;
import com.biblio.medialltech.entity.Category;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String image;
    private BookStatus status;
    private String borrowerUsername;
    private Category category;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String author, String image, BookStatus status, String borrowerUsername, Category category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.status = status;
        this.borrowerUsername = borrowerUsername;
        this.category = category;
    }

    // Getters et setters
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

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public String getBorrowerUsername() {
        return borrowerUsername;
    }

    public void setBorrowerUsername(String borrowerUsername) {
        this.borrowerUsername = borrowerUsername;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}