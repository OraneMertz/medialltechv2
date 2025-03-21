package com.biblio.medialltech.service.impl;

import com.biblio.medialltech.dto.BorrowingDTO;
import com.biblio.medialltech.entity.Book;
import com.biblio.medialltech.entity.BookStatus;
import com.biblio.medialltech.entity.Borrowing;
import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.mapper.BorrowingMapper;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.repository.BorrowingRepository;
import com.biblio.medialltech.repository.UserRepository;
import com.biblio.medialltech.service.BorrowingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowingServiceJpaImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingMapper borrowingMapper;

    public BorrowingServiceJpaImpl(BorrowingRepository borrowingRepository, BookRepository bookRepository, UserRepository userRepository, BorrowingMapper borrowingMapper) {
        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowingMapper = borrowingMapper;
    }

    @Transactional
    @Override
    public BorrowingDTO borrowBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé avec l'id : " + bookId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvée avec l'id : " + userId));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            return null;
        }
        book.setStatus(BookStatus.BORROWED);
        book.setBorrowerUsername(user.getUsername());
        bookRepository.save(book);

        Borrowing borrowing = new Borrowing(book, user, LocalDate.now());
        Borrowing savedBorrowing = borrowingRepository.save(borrowing);

        return borrowingMapper.toDTO(savedBorrowing);
    }

    @Transactional
    @Override
    public boolean returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé avec l'id : " + bookId));
        if (book.getStatus() != BookStatus.BORROWED) {
            return false;
        }

        book.setStatus(BookStatus.AVAILABLE);
        book.setBorrowerUsername(book.getBorrowerUsername());
        bookRepository.save(book);

        List<Borrowing> borrowings = borrowingRepository.findByBookId(bookId);
        if (!borrowings.isEmpty()) {
            Borrowing borrowing = borrowings.get(0);
            borrowing.setReturnDate(LocalDate.now());
        }

        return true;
    }

    public List<BorrowingDTO> getBorrowingsByUserId(Long userId) {
        return borrowingRepository.findByUserId(userId).stream()
                .map(borrowingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowingDTO> getBorrowingsByBookId(Long bookId) {
        return borrowingRepository.findByBookId(bookId).stream()
                .map(borrowingMapper::toDTO)
                .collect(Collectors.toList());
    }
}