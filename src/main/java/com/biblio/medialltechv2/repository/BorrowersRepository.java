package com.biblio.medialltechv2.repository;

import com.biblio.medialltechv2.model.Borrowers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowersRepository extends MongoRepository<Borrowers, String> {

}
