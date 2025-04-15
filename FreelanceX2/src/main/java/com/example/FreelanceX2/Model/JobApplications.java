package com.example.FreelanceX2.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;

@Data
public class JobApplications {

    @Id
    private String jobApplicationId;

    private String jobId;

    private long bidPrice;

    private String coverLetter;

    private LocalDate jobApplicationDate = LocalDate.now();

    @DBRef
    private Users applicant; // Reference to Users document


    public enum ApplicationStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    public ApplicationStatus getStatus() {
        return status;
    }
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    private  ApplicationStatus status =ApplicationStatus.PENDING;

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

    public Users getApplicant() {
        return applicant;
    }
    public void setApplicant(Users applicant) {
        this.applicant = applicant;
    }
}
