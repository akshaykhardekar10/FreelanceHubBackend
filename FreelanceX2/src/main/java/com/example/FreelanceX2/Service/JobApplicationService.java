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

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        }else if(jobApplicationsRepository.findByJobIdAndApplicant_Id(jobId,userId).isPresent()){
            throw new InvalidJobApplicationException("You have already applied for this job wait for the application to get accepted or rejected by the client .");
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

        public void updateApplicationStatus(String applicationId, String status, String currentUserId) {
            JobApplications application = jobApplicationsRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            Jobs job = jobsRepository.findById(application.getJobId())
                    .orElseThrow(() -> new RuntimeException("Related job not found"));

            // Only the job poster can update status
            if (!job.getPostedByUserId().equals(currentUserId)) {
                throw new RuntimeException("You are not authorized to update this application.");
            }

            // Convert string to enum safely
            JobApplications.ApplicationStatus newStatus;
            try {
                newStatus = JobApplications.ApplicationStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status value. Use PENDING, ACCEPTED, or REJECTED.");
            }

            // Prevent changing status if already final
            if (application.getStatus() != JobApplications.ApplicationStatus.PENDING) {
                throw new RuntimeException("Application already " + application.getStatus() + ". Cannot update.");
            }

            application.setStatus(newStatus);
            jobApplicationsRepository.save(application);

            // Update job availability
            if (newStatus == JobApplications.ApplicationStatus.ACCEPTED) {
                job.setAvailableNow(false);
            } else if (newStatus == JobApplications.ApplicationStatus.REJECTED) {
                // Only set true if no other PENDING or ACCEPTED applications exist
                boolean hasOtherAcceptedOrPending = jobApplicationsRepository.findAll().stream()
                        .anyMatch(app -> app.getJobId().equals(job.getJobId()) &&
                                (app.getStatus() == JobApplications.ApplicationStatus.PENDING
                                        || app.getStatus() == JobApplications.ApplicationStatus.ACCEPTED));

                job.setAvailableNow(hasOtherAcceptedOrPending);
            }

            jobsRepository.save(job);
        }

    public List<JobApplicationResponseDto> getApplicationsByJobIdForPoster(String jobId, String posterId) {
        // Check if job exists and is owned by the authenticated user
        Jobs job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getPostedByUserId().equals(posterId)) {
            throw new RuntimeException("You are not authorized to view applications for this job.");
        }

        List<JobApplications> applications = jobApplicationsRepository.findByJobId(jobId);

        return applications.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    public List<JobApplicationResponseDto> getApplicationsByUserId(String userId) {
        List<JobApplications> applications = jobApplicationsRepository.findByApplicant_Id(userId);
        return applications.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }



}
