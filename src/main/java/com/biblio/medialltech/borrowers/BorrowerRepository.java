package com.biblio.medialltech.borrowers;

import com.biblio.medialltech.books.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    List<Borrower> findByBorrowerUsernameContainingIgnoreCase(String username);

    List<Borrower> findByBookId(Long bookId);

    List<Borrower> findByStatus(BookStatus status);

    List<Borrower> findByCategoriesId(Long categoryId);

    List<Borrower> findByBorrowerUsername(String borrowerUsername);
}