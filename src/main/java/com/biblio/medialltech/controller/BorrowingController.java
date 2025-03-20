package com.biblio.medialltech.controller;

import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.repository.UserRepository;
import com.biblio.medialltech.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {

    private final BookService bookService;
    private final UserRepository userRepository;

    public BorrowingController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{bookId}/borrow/{userId}")
    public ResponseEntity<Void> borrowBook(@PathVariable User bookId, @PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            bookService.borrowBook(user.get().getId(), bookId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long bookId) {
        bookService.returnBook(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
