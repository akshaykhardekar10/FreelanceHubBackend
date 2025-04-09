package com.example.FreelanceX2.Model;


import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class JobApplications {

    @Id
    private String jobApplicationId;

    @NonNull
    private String proposalMessage;

    @NonNull
    private long expectedPrice;

    private LocalDate jobApplicationDate = LocalDate.now();
}
