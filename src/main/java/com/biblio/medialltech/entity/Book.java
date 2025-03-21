package com.biblio.medialltech.entity;

import jakarta.persistence.*;

@Entity(name = "books")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    @Column(name = "borrower_username")
    private String borrowerUsername;

    @ManyToOne
    @JoinTable(name = "books_categories",
            joinColumns = {@JoinColumn(name = "books_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private Category category;

    public Book() {
    }

    public Book(Long id, String title, String author, String image, BookStatus status, String borrowerUsername, Category category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.status = status;
        this.borrowerUsername = borrowerUsername;
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

    public void borrow(String username) {
        if (this.status == BookStatus.AVAILABLE) {
            this.status = BookStatus.BORROWED;
            this.borrowerUsername = username;
        } else {
            System.out.println("Le livre " + this.title + " est déjà emprunté.");
        }
    }

    public void returnBook() {
        if (this.borrowerUsername != null) {
            this.status = BookStatus.AVAILABLE;
            this.borrowerUsername = null;
        } else {
            System.out.println("Ce livre n'a pas été emprunté.");
        }
    }
}