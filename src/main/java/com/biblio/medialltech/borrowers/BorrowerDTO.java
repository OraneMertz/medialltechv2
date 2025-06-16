package com.biblio.medialltech.borrowers;

import java.time.LocalDate;
import java.util.List;

public class BorrowerDTO {

    private Long bookId;
    private String title;
    private String author;
    private String image;
    private String borrowerUsername;
    private LocalDate borrowDate;
    private List<Long> categories;
    private String status;

    public BorrowerDTO() {}

    public BorrowerDTO(Long bookId, String title, String author, String image,
                       String borrowerUsername, LocalDate borrowDate,
                       List<Long> categories, String status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.image = image;
        this.borrowerUsername = borrowerUsername;
        this.borrowDate = borrowDate;
        this.categories = categories;
        this.status = status;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

    public String getBorrowerUsername() {
        return borrowerUsername;
    }

    public void setBorrowerUsername(String borrowerUsername) {
        this.borrowerUsername = borrowerUsername;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}