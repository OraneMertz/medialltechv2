package com.biblio.medialltechv2.controller;

import com.biblio.medialltechv2.dto.BooksDTO;
import com.biblio.medialltechv2.model.Books;
import com.biblio.medialltechv2.service.BooksService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livres")
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<BooksDTO> getAllBooks() {
        return booksService.getAllBooks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BooksDTO> getBookById(@PathVariable String id) {
        return booksService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<BooksDTO> addBook(@RequestBody Books book) {
        BooksDTO createdBook = booksService.addBook(book);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<BooksDTO> updateBook(@PathVariable String id, @RequestBody Books bookDetails) {
        BooksDTO updatedBook = booksService.updateBook(id, bookDetails);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        booksService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}