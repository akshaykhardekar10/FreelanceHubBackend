package com.example.FreelanceX2.Repository;

import com.example.FreelanceX2.Model.JobApplications;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface JobApplicationsRepository extends MongoRepository<JobApplications , String> {
    Optional<JobApplications> findByJobIdAndApplicant_Id(String jobId, String applicantId);
    List<JobApplications> findByJobId(String jobId);
    List<JobApplications> findByApplicant_Id(String applicantId);


}
