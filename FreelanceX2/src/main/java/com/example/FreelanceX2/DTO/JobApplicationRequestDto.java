package com.example.FreelanceX2.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobApplicationRequestDto {

    private long bidPrice;

    private String coverLetter;

    private LocalDate jobApplicationDate = LocalDate.now();


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
}
