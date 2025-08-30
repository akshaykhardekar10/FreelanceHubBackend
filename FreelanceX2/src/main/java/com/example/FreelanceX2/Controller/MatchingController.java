package com.example.FreelanceX2.Controller;

import com.example.FreelanceX2.DTO.JobMatchResponseDto;
import com.example.FreelanceX2.DTO.MatchingRequestDto;
import com.example.FreelanceX2.DTO.UserMatchResponseDto;
import com.example.FreelanceX2.Model.Jobs;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Service.SemanticMatchingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    @Autowired
    private SemanticMatchingService semanticMatchingService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Find relevant jobs for a user based on semantic similarity
     */
    @GetMapping("/jobs-for-user/{userId}")
    public ResponseEntity<List<JobMatchResponseDto>> getRelevantJobsForUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<SemanticMatchingService.SimilarityResult<Jobs>> results = 
                semanticMatchingService.findRelevantJobsForUser(userId, limit);
            
            List<JobMatchResponseDto> response = results.stream()
                .map(result -> {
                    JobMatchResponseDto dto = modelMapper.map(result.getEntity(), JobMatchResponseDto.class);
                    dto.setSimilarityScore(result.getSimilarityScore());
                    
                    // Map the nested PostedBy object
                    if (result.getEntity().getPostedBy() != null) {
                        JobMatchResponseDto.PostedByDto postedByDto = new JobMatchResponseDto.PostedByDto();
                        postedByDto.setId(result.getEntity().getPostedBy().getId());
                        postedByDto.setUsername(result.getEntity().getPostedBy().getUsername());
                        postedByDto.setEmail(result.getEntity().getPostedBy().getEmail());
                        dto.setPostedBy(postedByDto);
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Find relevant users for a job based on semantic similarity
     */
    @GetMapping("/users-for-job/{jobId}")
    public ResponseEntity<List<UserMatchResponseDto>> getRelevantUsersForJob(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<SemanticMatchingService.SimilarityResult<Users>> results = 
                semanticMatchingService.findRelevantUsersForJob(jobId, limit);
            
            List<UserMatchResponseDto> response = results.stream()
                .map(result -> {
                    UserMatchResponseDto dto = modelMapper.map(result.getEntity(), UserMatchResponseDto.class);
                    dto.setSimilarityScore(result.getSimilarityScore());
                    return dto;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Find similar users based on profile similarity
     */
    @GetMapping("/similar-users/{userId}")
    public ResponseEntity<List<UserMatchResponseDto>> getSimilarUsers(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<SemanticMatchingService.SimilarityResult<Users>> results = 
                semanticMatchingService.findSimilarUsers(userId, limit);
            
            List<UserMatchResponseDto> response = results.stream()
                .map(result -> {
                    UserMatchResponseDto dto = modelMapper.map(result.getEntity(), UserMatchResponseDto.class);
                    dto.setSimilarityScore(result.getSimilarityScore());
                    return dto;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Find similar jobs based on job description similarity
     */
    @GetMapping("/similar-jobs/{jobId}")
    public ResponseEntity<List<JobMatchResponseDto>> getSimilarJobs(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<SemanticMatchingService.SimilarityResult<Jobs>> results = 
                semanticMatchingService.findSimilarJobs(jobId, limit);
            
            List<JobMatchResponseDto> response = results.stream()
                .map(result -> {
                    JobMatchResponseDto dto = modelMapper.map(result.getEntity(), JobMatchResponseDto.class);
                    dto.setSimilarityScore(result.getSimilarityScore());
                    
                    // Map the nested PostedBy object
                    if (result.getEntity().getPostedBy() != null) {
                        JobMatchResponseDto.PostedByDto postedByDto = new JobMatchResponseDto.PostedByDto();
                        postedByDto.setId(result.getEntity().getPostedBy().getId());
                        postedByDto.setUsername(result.getEntity().getPostedBy().getUsername());
                        postedByDto.setEmail(result.getEntity().getPostedBy().getEmail());
                        dto.setPostedBy(postedByDto);
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST endpoint for finding relevant jobs for a user (alternative to GET)
     */
    @PostMapping("/jobs-for-user")
    public ResponseEntity<List<JobMatchResponseDto>> findJobsForUser(@RequestBody MatchingRequestDto request) {
        return getRelevantJobsForUser(request.getEntityId(), request.getLimit());
    }

    /**
     * POST endpoint for finding relevant users for a job (alternative to GET)
     */
    @PostMapping("/users-for-job")
    public ResponseEntity<List<UserMatchResponseDto>> findUsersForJob(@RequestBody MatchingRequestDto request) {
        return getRelevantUsersForJob(request.getEntityId(), request.getLimit());
    }
}
