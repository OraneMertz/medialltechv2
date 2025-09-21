package com.biblio.medialltech.users;

import com.biblio.medialltech.users.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByPseudo(String pseudo);

    boolean existsByPseudo(String pseudo);

    boolean existsByEmail(String email);
}