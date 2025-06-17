package com.biblio.medialltech.borrowings;

import java.time.LocalDate;

public class BorrowingDTO {
    private Long id;
    private Long bookId;
    private String username;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowingDTO(Long id, Long bookId, String username, LocalDate borrowDate, LocalDate returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.username = username;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public BorrowingDTO() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "BorrowingDTO{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", username='" + username + '\'' +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                '}';
    }
}