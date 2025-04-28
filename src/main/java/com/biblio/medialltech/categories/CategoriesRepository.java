package com.biblio.medialltech.categories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Optional<Categories> findById(Long id);
    Optional<Categories> findByName(String name);
    boolean existsByName(String name);
}
