package com.biblio.medialltech.service.impl;

import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.service.BookService;
import org.springframework.stereotype.Service;
import com.biblio.medialltech.model.Book;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceJpaImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceJpaImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Book> getBooksByBorrowerId(Long borrowerId) {
        return bookRepository.findByBorrowerId(borrowerId);
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findByIsDisponible(true);
    }

    @Override
    public boolean borrowBook(Long bookId, Long userId) {
        return bookRepository.findById(bookId)
                .filter(Book::isDisponible)
                .map(book -> {
                    book.setDisponible(false);
                    book.setBorrowerId(userId);
                    bookRepository.save(book);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean returnBook(Long bookId) {
        return bookRepository.findById(bookId)
                .filter(book -> !book.isDisponible())
                .map(book -> {
                    book.setDisponible(true);
                    book.setBorrowerId(null);
                    bookRepository.save(book);
                    return true;
                })
                .orElse(false);
    }
}
