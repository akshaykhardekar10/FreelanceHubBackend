package com.example.FreelanceX2.Controller;

import com.example.FreelanceX2.Config.JwtUtil;
import com.example.FreelanceX2.DTO.*;
import com.example.FreelanceX2.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupDto signupDTO) {
        try {
              userService.registerUser(signupDTO);
              return ResponseEntity.ok("User registered successfully");

        }catch (Exception e){
            return ResponseEntity.badRequest().body("username or email already exist");
        }


    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenPairDTO> loginUser(@RequestBody LoginDto loginDTO , HttpServletResponse response) {

        JwtTokenPairDTO tokens = userService.loginUser(loginDTO);
        Cookie cookie = new Cookie("refreshToken",tokens.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenPairDTO> refreshToken(@RequestBody RefreshRequestDTO request) {
        JwtTokenPairDTO tokens = userService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }
}
