package com.example.FreelanceX2.Controller;

import com.example.FreelanceX2.DTO.ChatRequestDto;
import com.example.FreelanceX2.DTO.ChatResponseDto;
import com.example.FreelanceX2.Service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/xxx")
    public ResponseEntity<ChatResponseDto> chat(@RequestBody ChatRequestDto request) {
        String reply = chatBotService.getGeminiResponse(request.getMessage());
        ChatResponseDto response = new ChatResponseDto();
        response.setReply(reply);
        return ResponseEntity.ok(response);
    }
}

