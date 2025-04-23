package com.example.FreelanceX2.Controller;


import com.example.FreelanceX2.DTO.UserDto;
import com.example.FreelanceX2.DTO.UserProfileUpdateDto;
import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

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

    @PostMapping(value = "/uploadProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> uploadProfileImage(@RequestParam("image") MultipartFile imageFile) {
        // Retrieve current logged-in user from the security context.
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Delegate the image upload logic to the service.
        UserDto updatedUser = userService.uploadProfileImage(currentUser.getId(), imageFile);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/getCurrentUser")
    public ResponseEntity<UserDto> getCurrentUser() {
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDto response = modelMapper.map(currentUser, UserDto.class);
        return ResponseEntity.ok(response);
    }

}