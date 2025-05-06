package com.biblio.medialltech.books;

import java.util.List;

public class BookDTO {
    private String title;
    private String author;
    private String image;
    private List<Long> category;

    public BookDTO() {}

    public BookDTO(String title, String author, String image, List<Long> category) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.category = category;
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

    public List<Long> getCategory() {
        return category;
    }

    public void setCategoryIds(List<Long> category) {
        this.category = category;
    }
}
