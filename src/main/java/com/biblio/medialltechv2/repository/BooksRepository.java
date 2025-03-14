package com.biblio.medialltechv2.repository;

import com.biblio.medialltechv2.model.Books;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends MongoRepository<Books, String> {
}
