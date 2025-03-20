package com.biblio.medialltech.service.impl;

import com.biblio.medialltech.entity.Book;
import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.service.BorrowingService;

import java.util.Optional;

public class BorrowingServiceJpaImpl implements BorrowingService {

    private final BookRepository bookRepository;

    public BorrowingServiceJpaImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void borrowBook(User user, Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.isDisponible()) {
                book.borrow(user);
                bookRepository.save(book);
                System.out.println(user.getUsername() + " a emprunté le livre " + book.getTitle());
            } else {
                System.out.println("Le livre " + book.getTitle() + " n'est pas disponible.");
            }
        } else {
            System.out.println("Le livre avec l'ID " + bookId + " n'existe pas.");
        }
    }

    @Override
    public void returnBook(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.returnBook();
            bookRepository.save(book);
            System.out.println("Le livre " + book.getTitle() + " a été retourné.");
        } else {
            System.out.println("Le livre avec l'ID " + bookId + " n'existe pas.");
        }
    }
}