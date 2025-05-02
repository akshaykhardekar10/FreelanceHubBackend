package com.example.FreelanceX2.Controller;

import com.example.FreelanceX2.DTO.JobApplicationRequestDto;
import com.example.FreelanceX2.DTO.JobApplicationResponseDto;
import com.example.FreelanceX2.Model.JobApplications;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Service.JobApplicationService;
import com.example.FreelanceX2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobApplications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{jobId}/apply")
    public ResponseEntity<JobApplicationResponseDto> applyToJob(@PathVariable String jobId,
                                                                @RequestBody JobApplicationRequestDto dto) {


        Users users =(Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = users.getEmail();


        Optional<Users> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // User not found or unauthorized
        }

        Users user = userOptional.get();

        // Call the service to save the job application with the userId
        JobApplications saved = jobApplicationService.saveJobApplication(dto, jobId, user.getId());
        JobApplicationResponseDto responseDto = jobApplicationService.convertToResponseDto(saved);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/updateStatus/{applicationId}")
    public ResponseEntity<Map<String, String>> updateApplicationStatus(
            @PathVariable String applicationId,
            @RequestParam("value") String status,
            Principal principal) {

        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobApplicationService.updateApplicationStatus(applicationId, status, currentUser.getId());

        // Updated to return a JSON object
        Map<String, String> response = new HashMap<>();
        response.put("message", "Application status updated successfully.");
        return ResponseEntity.ok(response);  // Return as JSON object
    }


    @GetMapping("/job/{jobId}/applications")
    public ResponseEntity<List<JobApplicationResponseDto>> getApplicationsForJob(
            @PathVariable String jobId) {

        Users currentUser = (Users) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        List<JobApplicationResponseDto> applications = jobApplicationService
                .getApplicationsByJobIdForPoster(jobId, currentUser.getId());

        return ResponseEntity.ok(applications);
    }
    @GetMapping("/myApplications")
    public ResponseEntity<List<JobApplicationResponseDto>> getMyApplications() {
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobApplicationResponseDto> applications = jobApplicationService.getApplicationsByUserId(currentUser.getId());
        return ResponseEntity.ok(applications);
    }



}
