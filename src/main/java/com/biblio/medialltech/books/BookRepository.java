package com.biblio.medialltech.books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByCategoriesId(Long categoryId);
    List<Book> findByBorrowerUsername(String username);
    List<Book> findByStatus(BookStatus status);
}
