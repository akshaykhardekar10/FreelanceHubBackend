package com.example.FreelanceX2.DTO;

import com.example.FreelanceX2.Model.JobApplications;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JobApplicationResponseDto {

    private long bidPrice;
    private String coverLetter;
    private LocalDate jobApplicationDate;

    private String jobApplicationId;
    private String jobId;

    private JobApplications.ApplicationStatus status;

    private String applicantId;
    private String applicantUsername;
    private String applicantEmail;
    private List<String> skills;
    private String experience;
    private String githubLink;
    private String domain;
    private String bio;

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public JobApplications.ApplicationStatus getStatus() {
        return status;
    }
    public void setStatus(JobApplications.ApplicationStatus status) {
        this.status = status;
    }

    public long getBidPrice() {
        return bidPrice;
    }
    public void setBidPrice(long bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getCoverLetter() {
        return coverLetter;
    }
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public LocalDate getJobApplicationDate() {
        return jobApplicationDate;
    }
    public void setJobApplicationDate(LocalDate jobApplicationDate) {
        this.jobApplicationDate = jobApplicationDate;
    }

    public String getJobApplicationId() {
        return jobApplicationId;
    }
    public void setJobApplicationId(String jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
    }

    public String getJobId() {
        return jobId;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getApplicantId() {
        return applicantId;
    }
    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantUsername() {
        return applicantUsername;
    }
    public void setApplicantUsername(String applicantUsername) {
        this.applicantUsername = applicantUsername;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }
    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
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
