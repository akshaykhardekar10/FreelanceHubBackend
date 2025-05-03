package com.example.FreelanceX2.DTO;


import lombok.Data;

@Data
public class ChatRequestDto {
    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
