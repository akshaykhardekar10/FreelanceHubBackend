package com.example.FreelanceX2.Service;

import com.example.FreelanceX2.DTO.JobApplicationRequestDto;
import com.example.FreelanceX2.DTO.JobApplicationResponseDto;
import com.example.FreelanceX2.Model.JobApplications;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Repository.JobApplicationsRepository;
import com.example.FreelanceX2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public JobApplications saveJobApplication(JobApplicationRequestDto dto, String jobId, String userId) {
        Optional<Users> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Users applicant = userOpt.get();

        JobApplications application = modelMapper.map(dto, JobApplications.class);
        application.setJobId(jobId);
        application.setApplicant(applicant);

        return jobApplicationsRepository.save(application);
    }

    public JobApplicationResponseDto convertToResponseDto(JobApplications entity) {
        JobApplicationResponseDto dto = modelMapper.map(entity, JobApplicationResponseDto.class);

        Users user = entity.getApplicant();
        dto.setApplicantId(user.getId());
        dto.setApplicantUsername(user.getUsername());
        dto.setApplicantEmail(user.getEmail());
        dto.setSkills(user.getSkills());
        dto.setExperience(user.getExperience());
        dto.setGithubLink(user.getGithubLink());

        return dto;
    }
}
