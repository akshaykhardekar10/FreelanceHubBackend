package com.example.FreelanceX2.DTO;

import lombok.Data;

@Data
public class ChatResponseDto {
    private String reply;


    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
