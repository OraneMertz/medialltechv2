package com.biblio.medialltech.repository;

import com.biblio.medialltech.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.biblio.medialltech.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByCategoryId(Long categoryId);
    List<Book> findByBorrowerUsername(String username);
    List<Book> findByStatus(BookStatus status);
}
