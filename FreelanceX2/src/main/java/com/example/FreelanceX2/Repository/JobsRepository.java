package com.example.FreelanceX2.Repository;


import com.example.FreelanceX2.Model.Jobs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobsRepository extends MongoRepository<Jobs, String > {
    List<Jobs> findByAvailableNowTrue();
    @Query("{ 'postedByUserId' : ?0 }")
    List<Jobs> findByPostedByUserId(String postedByUserId);
}
