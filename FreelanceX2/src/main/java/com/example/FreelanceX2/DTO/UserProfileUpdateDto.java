package com.example.FreelanceX2.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileUpdateDto {
    private List<String> skills;
    private String experience;
    private String githubLink;
    private String domain;


    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
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

    public String getGithubLink() {
        return githubLink;
    }
    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }
}
