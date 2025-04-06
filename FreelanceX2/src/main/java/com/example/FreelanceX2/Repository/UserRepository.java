package com.example.FreelanceX2.Repository;

import com.example.FreelanceX2.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<Users,String> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    Optional<Users> findById(String id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
