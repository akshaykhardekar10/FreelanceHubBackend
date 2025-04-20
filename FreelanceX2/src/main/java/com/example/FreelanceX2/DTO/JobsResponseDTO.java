package com.example.FreelanceX2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data

public class JobsResponseDTO {
    private String jobid;
    private String title;
    private String description;
    private long price;
    private LocalDate date;

    private boolean availableNow;
    private String postedById;
    private String postedByUsername;
    private String postedByEmail;
    private String jobDomain;

    public String getJobid() {
        return jobid;
    }
    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getJobDomain() {
        return jobDomain;
    }
    public void setJobDomain(String jobDomain) {
        this.jobDomain = jobDomain;
    }

    public String getJobId() {
        return jobid;
    }
    public void setJobId(String jobid) {
        this.jobid = jobid;
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

    public boolean isAvailableNow() {
        return availableNow;
    }
    public void setAvailableNow(boolean availableNow) {
        this.availableNow = availableNow;
    }

    public String getPostedById() {
        return postedById;
    }
    public void setPostedById(String postedById) {
        this.postedById = postedById;
    }

    public String getPostedByUsername() {
        return postedByUsername;
    }
    public void setPostedByUsername(String postedByUsername) {
        this.postedByUsername = postedByUsername;
    }

    public String getPostedByEmail() {
        return postedByEmail;
    }
    public void setPostedByEmail(String postedByEmail) {
        this.postedByEmail = postedByEmail;
    }

    public JobsResponseDTO() {
    }

    public JobsResponseDTO(String jobid, String title, String description, long price, LocalDate date, boolean availableNow, String postedById, String postedByUsername, String postedByEmail) {
        this.jobid = jobid;
        this.title = title;
        this.description = description;
        this.price = price;
        this.date = date;
        this.availableNow = availableNow;
        this.postedById = postedById;
        this.postedByUsername = postedByUsername;
        this.postedByEmail = postedByEmail;
    }
}
