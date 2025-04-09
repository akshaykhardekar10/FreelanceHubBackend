package com.example.FreelanceX2.Service;

import com.example.FreelanceX2.DTO.JobsRequestDto;
import com.example.FreelanceX2.DTO.JobsResponseDTO;
import com.example.FreelanceX2.Model.Jobs;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Repository.JobsRepository;
import com.example.FreelanceX2.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobsService {

    @Autowired
    private JobsRepository jobsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<JobsResponseDTO> getAllJobs() {
        return jobsRepository.findAll()
                .stream()
                .map(this::mapToJobResponse)
                .collect(Collectors.toList());
    }

    public List<JobsResponseDTO> getAvailableJobs() {
        return jobsRepository.findByAvailableNowTrue()
                .stream()
                .map(this::mapToJobResponse)
                .collect(Collectors.toList());
    }

    public Jobs postJob(JobsRequestDto dto, Users user) {
        Jobs job = modelMapper.map(dto, Jobs.class);

        job.setAvailableNow(true);
        job.setDate(LocalDate.now());

        job.setPostedByUserId(user.getId());

        Jobs.PostedBy postedBy = new Jobs.PostedBy();
        postedBy.setId(user.getId());
        postedBy.setUsername(user.getUsername());
        postedBy.setEmail(user.getEmail());
        job.setPostedBy(postedBy);

        Jobs savedJobs= jobsRepository.save(job);
        user.getJobsPosted().add(savedJobs);
        userRepository.save(user);
        return savedJobs;
    }

    private JobsResponseDTO mapToJobResponse(Jobs job) {
        return modelMapper.map(job, JobsResponseDTO.class);
    }

    public  List<JobsResponseDTO> getJobsPostedByMe(Users users){
        List<Jobs> jobs =   jobsRepository.findByPostedByUserId(users.getId());

        return jobs.stream()
                .map(job -> modelMapper.map(job,JobsResponseDTO.class))
                .collect(Collectors.toList());
    }
}
