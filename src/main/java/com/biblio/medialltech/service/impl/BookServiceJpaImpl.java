package com.biblio.medialltech.service.impl;

import com.biblio.medialltech.dto.BookDTO;
import com.biblio.medialltech.entity.Book;
import com.biblio.medialltech.entity.BookStatus;
import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.mapper.BookMapper;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.repository.CategoryRepository;
import com.biblio.medialltech.service.BookService;
import org.springframework.stereotype.Service;

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

    private BookDTO getBookDTO(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);

        return getBookDTO(book, book.getCategory());
    }

    private BookDTO getBookDTO(Book book, Category category2) {
        if (category2 != null && category2.getId() != null) {
            Optional<Category> category = categoryRepository.findById(category2.getId());
            category.ifPresent(book::setCategory);
        }

        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
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
        existingBook.setStatus(bookDTO.getStatus());

        return getBookDTO(existingBook, bookDTO.getCategory());
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
    public List<BookDTO> getBooksByBorrower(String borrowerUsername) {
        return bookRepository.findByBorrowerUsername(borrowerUsername).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findByStatus(BookStatus.AVAILABLE).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean borrowBook(Long bookId, String username) {
        return bookRepository.findById(bookId)
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .map(book -> {
                    book.setStatus(BookStatus.BORROWED);
                    book.setBorrowerUsername(username);
                    bookRepository.save(book);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean returnBook(Long bookId) {
        return bookRepository.findById(bookId)
                .filter(book -> book.getStatus() == BookStatus.BORROWED)
                .map(book -> {
                    book.setStatus(BookStatus.AVAILABLE);
                    book.setBorrowerUsername(null);
                    bookRepository.save(book);
                    return true;
                })
                .orElse(false);
    }
}