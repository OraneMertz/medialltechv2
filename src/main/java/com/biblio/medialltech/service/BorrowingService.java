package com.biblio.medialltech.service;

import com.biblio.medialltech.dto.BorrowingDTO;
import org.springframework.transaction.annotation.Transactional;

public interface BorrowingService {
    @Transactional
    BorrowingDTO borrowBook(Long bookId, Long userId);

    @Transactional
    boolean returnBook(Long bookId);
}
