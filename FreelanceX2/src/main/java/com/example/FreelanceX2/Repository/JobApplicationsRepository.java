package com.example.FreelanceX2.Repository;

import com.example.FreelanceX2.Model.JobApplications;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JobApplicationsRepository extends MongoRepository<JobApplications , String> {

}
