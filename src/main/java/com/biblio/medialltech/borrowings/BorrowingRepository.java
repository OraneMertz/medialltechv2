package com.biblio.medialltech.borrowings;

import com.biblio.medialltech.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByBookIn(List<Book> books);
    List<Borrowing> findByBookId(Long bookId);
    List<Borrowing> findByUserUsername(String username);
}
