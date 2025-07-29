package com.innocito.axcl.repository;

import com.innocito.axcl.entity.Language;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends MongoRepository<Language, String> {
    @Query(collation = "{'locale': 'en_US', 'strength': 2}")
    Optional<Language> findByName(String name);

    @Query(collation = "{'locale': 'en_US', 'strength': 2}")
    boolean existsByName(String name);
}