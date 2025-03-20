package com.biblio.medialltech.controller;

import com.biblio.medialltech.dto.BookDTO;
import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.mapper.BookMapper;
import com.biblio.medialltech.service.BookService;
import com.biblio.medialltech.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<BookDTO> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BookDTO>> getBooksByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(bookService.getBookByCategory(categoryId));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<List<BookDTO>> getBooksByBorrower(@PathVariable Long borrowerId) {
        return ResponseEntity.ok(bookService.getBooksByBorrowerId(borrowerId));
    }

    @PostMapping("/create")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return updatedBook != null ? ResponseEntity.ok(updatedBook) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{bookId}/borrow/{userId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long bookId, @PathVariable User userId) {
        if (bookService.borrowBook(bookId, userId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long bookId) {
        if (bookService.returnBook(bookId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
