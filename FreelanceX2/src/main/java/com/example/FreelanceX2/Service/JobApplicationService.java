package com.example.FreelanceX2.Service;

import com.example.FreelanceX2.DTO.JobApplicationRequestDto;
import com.example.FreelanceX2.DTO.JobApplicationResponseDto;
import com.example.FreelanceX2.DTO.JobsResponseDTO;
import com.example.FreelanceX2.Exception.InvalidJobApplicationException;
import com.example.FreelanceX2.Model.JobApplications;
import com.example.FreelanceX2.Model.Jobs;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Repository.JobApplicationsRepository;
import com.example.FreelanceX2.Repository.JobsRepository;
import com.example.FreelanceX2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    @Autowired
    private  JobApplicationsRepository jobApplicationsRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private JobsRepository jobsRepository;
    @Autowired
    private JobsService jobsService;


    public JobApplications saveJobApplication(JobApplicationRequestDto dto, String jobId, String userId) {
        Optional<Users> userOpt = userRepository.findById(userId);

        Users currentUser =(Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobsResponseDTO> myPostedJobs = jobsService.getJobsPostedByMe(currentUser);

        Jobs job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));



        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }else  if (job.getPostedByUserId().equals(currentUser.getId())) {
            throw new InvalidJobApplicationException("You cannot apply to your own Posted job.");
        }

        Users applicant = userOpt.get();

        JobApplications application = modelMapper.map(dto, JobApplications.class);
        application.setJobId(jobId);
        application.setApplicant(applicant);
        application.setStatus(JobApplications.ApplicationStatus.PENDING);

        return jobApplicationsRepository.save(application);
    }

    public JobApplicationResponseDto convertToResponseDto(JobApplications jobApplications) {
        JobApplicationResponseDto dto = modelMapper.map(jobApplications, JobApplicationResponseDto.class);

        Users user = jobApplications.getApplicant();
        dto.setApplicantId(user.getId());
        dto.setApplicantUsername(user.getUsername());
        dto.setApplicantEmail(user.getEmail());
        dto.setSkills(user.getSkills());
        dto.setExperience(user.getExperience());
        dto.setGithubLink(user.getGithubLink());
        dto.setStatus(jobApplications.getStatus());


        return dto;
    }
}
