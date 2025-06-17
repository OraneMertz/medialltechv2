package com.biblio.medialltech.books;

import java.util.List;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String image;
    private List<Long> categoryIds;
    private BookStatus status;
    private String borrowerUsername;

    public BookDTO() {}

    public BookDTO(Long id, String title, String author, String image, List<Long> categoryIds, BookStatus status, String borrowerUsername) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.categoryIds = categoryIds;
        this.status = status;
        this.borrowerUsername = borrowerUsername;
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

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
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

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", image='" + image + '\'' +
                ", categoryIds=" + categoryIds +
                ", status=" + status +
                ", borrowerUsername='" + borrowerUsername + '\'' +
                '}';
    }
}