package com.example.FreelanceX2.Controller;

import com.example.FreelanceX2.DTO.*;
import com.example.FreelanceX2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<JwtTokenPairDTO> loginUser(@RequestBody LoginDto loginDTO) {

        JwtTokenPairDTO tokens = userService.loginUser(loginDTO);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenPairDTO> refreshToken(@RequestBody RefreshRequestDTO request) {
        JwtTokenPairDTO tokens = userService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }
}
