package com.biblio.medialltech.entity;

import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column
    private String image;

    @Column(name = "is_disponible", nullable = false)
    private boolean isDisponible = true;

    @Column
    private Long borrowerId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Book () {}

    public Book(Long id, String title, String author, String image, boolean isDisponible, Long borrowerId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.isDisponible = isDisponible;
        this.borrowerId = borrowerId;
    }

    public Book(Long id, String title, String author, String image, boolean isDisponible, Long borrowerId, Category category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.isDisponible = isDisponible;
        this.borrowerId = borrowerId;
        this.category = category;
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

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCategory(Optional<Category> category) {
        if (category.isPresent()) {
            this.category = category.get();
        } else {
            this.category = null;
        }
    }
}
