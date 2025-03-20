package com.biblio.medialltech.service.impl;

import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.repository.CategoryRepository;
import com.biblio.medialltech.service.BookService;
import org.springframework.stereotype.Service;
import com.biblio.medialltech.entity.Book;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceJpaImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookServiceJpaImpl(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
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
    public List<Book> getBookByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Book createBook(Book book) {
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(book.getCategory().getId());
            category.ifPresent(book::setCategory);
        }
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
    public boolean borrowBook(Long bookId, User userId) {
        return bookRepository.findById(bookId)
                .filter(Book::isDisponible)
                .map(book -> {
                    book.setDisponible(false);
                    book.setBorrower(userId);
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
                    book.setBorrower(null);
                    bookRepository.save(book);
                    return true;
                })
                .orElse(false);
    }
}
