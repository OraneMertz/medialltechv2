package com.biblio.medialltech.users;

import com.biblio.medialltech.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPseudo(String pseudo);

    boolean existsByPseudo(String pseudo);

    boolean existsByEmail(String email);
}
