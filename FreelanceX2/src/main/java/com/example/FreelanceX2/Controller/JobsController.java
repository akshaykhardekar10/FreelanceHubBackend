package com.example.FreelanceX2.Controller;


import com.example.FreelanceX2.DTO.JobsRequestDto;
import com.example.FreelanceX2.DTO.JobsResponseDTO;
import com.example.FreelanceX2.Model.Jobs;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Repository.JobsRepository;
import com.example.FreelanceX2.Repository.UserRepository;
import com.example.FreelanceX2.Service.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobsController {

    @Autowired
    private JobsService jobsService;

    @Autowired
    private JobsRepository jobsRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/getAllJobs")
    public ResponseEntity<List<JobsResponseDTO>> getAllJobs(){
        List<JobsResponseDTO> allJobs= jobsService.getAllJobs();
        return  ResponseEntity.ok(allJobs);
    }

    @GetMapping("/getAvailableJobs")
    public ResponseEntity<List<JobsResponseDTO>> getAvailableJobs(){
        List<JobsResponseDTO> availableJobs = jobsService.getAvailableJobs();
        return ResponseEntity.ok(availableJobs);
    }

    @PostMapping("/postJob")
    public ResponseEntity<JobsResponseDTO> postJob(@RequestBody JobsRequestDto jobsRequestDto){
        Users users =(Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Jobs job = jobsService.postJob(jobsRequestDto, users);

        JobsResponseDTO response = new JobsResponseDTO();
        response.setJobId(job.getJobId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setPrice(job.getPrice());
        response.setDate(job.getDate());
        response.setAvailableNow(job.isAvailableNow());
        response.setPostedById(job.getPostedBy().getId());
        response.setPostedByUsername(job.getPostedBy().getUsername());
        response.setPostedByEmail(job.getPostedBy().getEmail());
        response.setJobDomain(job.getJobDomain());

        return ResponseEntity.ok(response);

    }

    @GetMapping("/getJobsPostedByMe")
    public ResponseEntity<List<JobsResponseDTO>>  getMyPostedJobs(){
       Users currentUser =(Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       List<JobsResponseDTO> myPostedJobs = jobsService.getJobsPostedByMe(currentUser);
       return  ResponseEntity.ok(myPostedJobs);
    }
}
