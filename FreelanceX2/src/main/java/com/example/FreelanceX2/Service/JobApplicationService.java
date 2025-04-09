package com.example.FreelanceX2.Service;


import com.example.FreelanceX2.Repository.JobApplicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationsRepository jobApplicationsRepository;


}
