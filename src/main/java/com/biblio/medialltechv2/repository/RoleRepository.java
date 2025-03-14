package com.biblio.medialltechv2.repository;

import com.biblio.medialltechv2.model.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends MongoRepository<Roles, String> {
    List<Roles> findByNameIn(List<Roles> names);
    Roles findByName(String name);
}
