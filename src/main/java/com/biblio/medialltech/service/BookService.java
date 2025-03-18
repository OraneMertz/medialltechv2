package com.biblio.medialltech.service;

import com.biblio.medialltech.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    List<Book> getBooksByAuthor(String author);

    Book createBook(Book book);

    Book updateBook(Book book);

    boolean deleteBook(Long id);

    List<Book> getBooksByBorrowerId(Long borrowerId);

    List<Book> getAvailableBooks();

    boolean borrowBook(Long bookId, Long userId);

    boolean returnBook(Long bookId);
}
