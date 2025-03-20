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

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @ManyToOne
    @JoinTable( name = "books_categories",
                joinColumns = {@JoinColumn(name = "books_id")},
                inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private Category category;

    public Book () {}

    public Book(Long id, String title, String author, String image, boolean isDisponible, User borrower) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.isDisponible = isDisponible;
        this.borrower = borrower;
    }

    public Book(Long id, String title, String author, String image, boolean isDisponible, User borrower, Category category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.isDisponible = isDisponible;
        this.borrower = borrower;
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

    public boolean isDisponible() {
        return isDisponible;
    }

    public void setDisponible(boolean disponible) {
        isDisponible = disponible;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void borrow(User user) {
        if (this.isDisponible) {
            this.isDisponible = false;
            this.borrower = user;
        } else {
            System.out.println("Le livre " + this.title + " est déjà emprunté.");
        }
    }

    public void returnBook() {
        if (this.borrower != null) {
            this.isDisponible = true;
            this.borrower = null;
        } else {
            System.out.println("Ce livre n'a pas été emprunté.");
        }
    }
}
