package com.example.FreelanceX2.DTO;

import lombok.Data;

@Data
public class JobsRequestDto {
    private String title;
    private String description;
    private long price;
    private String jobDomain;

    public String getJobDomain() {
        return jobDomain;
    }
    public void setJobDomain(String jobDomain) {
        this.jobDomain = jobDomain;
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
}
