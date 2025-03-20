package com.biblio.medialltech.service;

import com.biblio.medialltech.dto.BookDTO;
import com.biblio.medialltech.entity.Book;
import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.entity.User;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDTO> getAllBooks();
    Optional<BookDTO> getBookById(Long id);
    List<BookDTO> getBooksByAuthor(String author);
    List<BookDTO> getBookByCategory(Long categoryId);
    Optional<Category> getCategoryById(Long categoryId);
    BookDTO createBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    boolean deleteBook(Long id);
    List<BookDTO> getBooksByBorrowerId(Long borrowerId);
    List<BookDTO> getAvailableBooks();
    boolean borrowBook(Long bookId, User userId);
    boolean returnBook(Long bookId);
}
