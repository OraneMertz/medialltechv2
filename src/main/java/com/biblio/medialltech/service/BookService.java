package com.biblio.medialltech.service;

import com.biblio.medialltech.entity.Book;
import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.entity.User;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    List<Book> getBooksByAuthor(String author);

    List<Book> getBookByCategory(Long categoryId);

    Optional<Category> getCategoryById(Long categoryId);

    Book createBook(Book book);

    Book updateBook(Book book);

    boolean deleteBook(Long id);

    List<Book> getBooksByBorrowerId(Long borrowerId);

    List<Book> getAvailableBooks();

    boolean borrowBook(Long bookId, User userId);

    boolean returnBook(Long bookId);
}
