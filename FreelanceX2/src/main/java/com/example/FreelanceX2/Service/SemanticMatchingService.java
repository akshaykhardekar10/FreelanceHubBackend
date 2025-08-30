package com.example.FreelanceX2.Service;

import com.example.FreelanceX2.Model.Jobs;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Repository.JobsRepository;
import com.example.FreelanceX2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemanticMatchingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobsRepository jobsRepository;

    @Autowired
    private EmbeddingService embeddingService;

    public static class SimilarityResult<T> {
        private T entity;
        private double similarityScore;

        public SimilarityResult(T entity, double similarityScore) {
            this.entity = entity;
            this.similarityScore = similarityScore;
        }

        public T getEntity() {
            return entity;
        }

        public void setEntity(T entity) {
            this.entity = entity;
        }

        public double getSimilarityScore() {
            return similarityScore;
        }

        public void setSimilarityScore(double similarityScore) {
            this.similarityScore = similarityScore;
        }
    }

    /**
     * Find the most relevant jobs for a given user based on semantic similarity
     */
    public List<SimilarityResult<Jobs>> findRelevantJobsForUser(String userId, int limit) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getProfileEmbedding() == null || user.getProfileEmbedding().isEmpty()) {
            throw new RuntimeException("User profile embedding not found. Please update your profile.");
        }

        List<Jobs> jobsWithEmbeddings = jobsRepository.findJobsWithEmbeddings();
        List<SimilarityResult<Jobs>> similarities = new ArrayList<>();

        for (Jobs job : jobsWithEmbeddings) {
            if (job.getJobEmbedding() != null && !job.getJobEmbedding().isEmpty()) {
                // Skip jobs posted by the same user
                if (!job.getPostedByUserId().equals(userId)) {
                    double similarity = embeddingService.calculateCosineSimilarity(
                            user.getProfileEmbedding(), 
                            job.getJobEmbedding()
                    );
                    similarities.add(new SimilarityResult<>(job, similarity));
                }
            }
        }

        return similarities.stream()
                .sorted(Comparator.comparingDouble(SimilarityResult<Jobs>::getSimilarityScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Find the most relevant users for a given job based on semantic similarity
     */
    public List<SimilarityResult<Users>> findRelevantUsersForJob(String jobId, int limit) {
        Jobs job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        if (job.getJobEmbedding() == null || job.getJobEmbedding().isEmpty()) {
            throw new RuntimeException("Job embedding not found. Please recreate the job posting.");
        }

        List<Users> usersWithEmbeddings = userRepository.findUsersWithEmbeddings();
        List<SimilarityResult<Users>> similarities = new ArrayList<>();

        for (Users user : usersWithEmbeddings) {
            if (user.getProfileEmbedding() != null && !user.getProfileEmbedding().isEmpty()) {
                // Skip the user who posted the job
                if (!user.getId().equals(job.getPostedByUserId())) {
                    double similarity = embeddingService.calculateCosineSimilarity(
                            job.getJobEmbedding(), 
                            user.getProfileEmbedding()
                    );
                    similarities.add(new SimilarityResult<>(user, similarity));
                }
            }
        }

        return similarities.stream()
                .sorted(Comparator.comparingDouble(SimilarityResult<Users>::getSimilarityScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Find similar users based on profile similarity
     */
    public List<SimilarityResult<Users>> findSimilarUsers(String userId, int limit) {
        Users targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (targetUser.getProfileEmbedding() == null || targetUser.getProfileEmbedding().isEmpty()) {
            throw new RuntimeException("User profile embedding not found. Please update your profile.");
        }

        List<Users> usersWithEmbeddings = userRepository.findUsersWithEmbeddings();
        List<SimilarityResult<Users>> similarities = new ArrayList<>();

        for (Users user : usersWithEmbeddings) {
            if (user.getProfileEmbedding() != null && !user.getProfileEmbedding().isEmpty()) {
                // Skip the target user themselves
                if (!user.getId().equals(userId)) {
                    double similarity = embeddingService.calculateCosineSimilarity(
                            targetUser.getProfileEmbedding(), 
                            user.getProfileEmbedding()
                    );
                    similarities.add(new SimilarityResult<>(user, similarity));
                }
            }
        }

        return similarities.stream()
                .sorted(Comparator.comparingDouble(SimilarityResult<Users>::getSimilarityScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Find similar jobs based on job description similarity
     */
    public List<SimilarityResult<Jobs>> findSimilarJobs(String jobId, int limit) {
        Jobs targetJob = jobsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        if (targetJob.getJobEmbedding() == null || targetJob.getJobEmbedding().isEmpty()) {
            throw new RuntimeException("Job embedding not found. Please recreate the job posting.");
        }

        List<Jobs> jobsWithEmbeddings = jobsRepository.findJobsWithEmbeddings();
        List<SimilarityResult<Jobs>> similarities = new ArrayList<>();

        for (Jobs job : jobsWithEmbeddings) {
            if (job.getJobEmbedding() != null && !job.getJobEmbedding().isEmpty()) {
                // Skip the target job itself
                if (!job.getJobId().equals(jobId)) {
                    double similarity = embeddingService.calculateCosineSimilarity(
                            targetJob.getJobEmbedding(), 
                            job.getJobEmbedding()
                    );
                    similarities.add(new SimilarityResult<>(job, similarity));
                }
            }
        }

        return similarities.stream()
                .sorted(Comparator.comparingDouble(SimilarityResult<Jobs>::getSimilarityScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
