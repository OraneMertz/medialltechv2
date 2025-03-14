package com.biblio.medialltechv2.repository;

import com.biblio.medialltechv2.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, String> {
    Users getUserByUsername(String username);
}
