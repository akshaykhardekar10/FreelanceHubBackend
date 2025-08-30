package com.example.FreelanceX2.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserMatchResponseDto {
    private String id;
    private String username;
    private String email;
    private List<String> skills;
    private String experience;
    private String domain;
    private String bio;
    private double userRatings;
    private double similarityScore;
    private String profileImageUrl;
    
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
    
    public List<String> getSkills() {
        return skills;
    }
    
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    
    public String getExperience() {
        return experience;
    }
    
    public void setExperience(String experience) {
        this.experience = experience;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public double getUserRatings() {
        return userRatings;
    }
    
    public void setUserRatings(double userRatings) {
        this.userRatings = userRatings;
    }
    
    public double getSimilarityScore() {
        return similarityScore;
    }
    
    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
