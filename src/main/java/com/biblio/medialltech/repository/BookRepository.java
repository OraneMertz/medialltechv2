package com.biblio.medialltech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.biblio.medialltech.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBorrowerId(Long borrowerId);
    List<Book> findByIsDisponible(Boolean isDisponible);
    List<Book> findByAuthorContainingIgnoreCase(String author);
}
