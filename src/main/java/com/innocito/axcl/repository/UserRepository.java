package com.innocito.axcl.repository;

import com.innocito.axcl.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query(collation = "{'locale': 'en_US', 'strength': 2}")
    Optional<User> findByEmail(String email);

    Optional<User> findByMobileNumber(String mobileNumber);

    Optional<User> findByMobileNumberAndUserType(String mobileNumber, Integer userType);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    @Aggregation(pipeline = {
            "{ '$match': { '_id': :#{loggedInUserId} } }",
            "{ '$project': { '_id': 0, 'gender': 1 } }"
    })
    Optional<User> findByIdAndProjectGender(ObjectId loggedInUserId);

    Optional<User> findByIdAndUserStatus(String userId, int status);
}