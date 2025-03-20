package com.biblio.medialltech.service;

import com.biblio.medialltech.entity.User;

public interface BorrowingService {
    void borrowBook(User user, Long bookId);

    void returnBook(Long bookId);
}
