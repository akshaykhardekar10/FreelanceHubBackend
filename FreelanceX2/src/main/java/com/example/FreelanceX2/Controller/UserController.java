package com.example.FreelanceX2.Controller;


import com.example.FreelanceX2.DTO.UserDto;
import com.example.FreelanceX2.DTO.UserProfileUpdateDto;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){
       List<UserDto>allUsers= userService.getAllUsers();
       return  ResponseEntity.ok(allUsers);
    }


    @PutMapping("/updateProfile")
    public ResponseEntity<UserDto> updateUserProfile(@RequestBody UserProfileUpdateDto updateDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) auth.getPrincipal();

        UserDto updatedUserDto = userService.updateUserProfile(currentUser.getId(), updateDto);
        return ResponseEntity.ok(updatedUserDto);
    }

}
