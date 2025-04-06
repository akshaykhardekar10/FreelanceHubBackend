package com.example.FreelanceX2.Model;


import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class Users {
    @Id
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;

    @DBRef
    private List<Jobs> jobsPosted =new ArrayList<>();

    private List<String> skills =new ArrayList<>();
    private String experience;
    private String githubLink;

    public String getGithubLink() {
        return githubLink;
    }
    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
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

    public List<Jobs> getJobsPosted() {
        return jobsPosted;
    }
    public void setJobsPosted(List<Jobs> jobsPosted) {
        this.jobsPosted = jobsPosted;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Users(String id, @NonNull String password, @NonNull String email, @NonNull String username) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.username = username;
    }
    public Users() {

    }
}
