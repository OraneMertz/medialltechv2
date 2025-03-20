package com.biblio.medialltech.service.impl;

import com.biblio.medialltech.dto.BookDTO;
import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.mapper.BookMapper;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.repository.CategoryRepository;
import com.biblio.medialltech.service.BookService;
import org.springframework.stereotype.Service;
import com.biblio.medialltech.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceJpaImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public BookServiceJpaImpl(BookRepository bookRepository, CategoryRepository categoryRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDTO> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDTO);
    }

    @Override
    public List<BookDTO> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBookByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        return getBookDTO(bookDTO);
    }

    private BookDTO getBookDTO(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);

        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(book.getCategory().getId());
            category.ifPresent(book::setCategory);
        }

        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Optional<Book> existingBookOpt = bookRepository.findById(id);

        if (existingBookOpt.isEmpty()) {
            return null;
        }

        Book existingBook = existingBookOpt.get();

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setImage(bookDTO.getImage());
        existingBook.setDisponible(bookDTO.isDisponible());

        if (bookDTO.getCategory() != null && bookDTO.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(bookDTO.getCategory().getId());
            category.ifPresent(existingBook::setCategory);
        }

        if (bookDTO.getBorrower() != null) {
            existingBook.setBorrower(bookDTO.getBorrower());
        }

        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDTO(updatedBook);
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
    public List<BookDTO> getBooksByBorrowerId(Long borrowerId) {
        return bookRepository.findByBorrowerId(borrowerId).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findByIsDisponible(true).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
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