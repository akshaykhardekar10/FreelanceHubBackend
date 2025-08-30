package com.example.FreelanceX2.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobMatchResponseDto {
    private String jobId;
    private String title;
    private String description;
    private long price;
    private LocalDate date;
    private String jobDomain;
    private double similarityScore;
    private PostedByDto postedBy;
    
    @Data
    public static class PostedByDto {
        private String id;
        private String username;
        private String email;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
    
    public String getJobId() {
        return jobId;
    }
    
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public long getPrice() {
        return price;
    }
    
    public void setPrice(long price) {
        this.price = price;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getJobDomain() {
        return jobDomain;
    }
    
    public void setJobDomain(String jobDomain) {
        this.jobDomain = jobDomain;
    }
    
    public double getSimilarityScore() {
        return similarityScore;
    }
    
    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }
    
    public PostedByDto getPostedBy() {
        return postedBy;
    }
    
    public void setPostedBy(PostedByDto postedBy) {
        this.postedBy = postedBy;
    }
}
