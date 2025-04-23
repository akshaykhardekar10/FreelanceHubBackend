package com.example.FreelanceX2.Service;

import com.example.FreelanceX2.Config.JwtUtil;
import com.example.FreelanceX2.DTO.*;

import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(SignupDto signupDTO) {
        boolean emailExists = userRepository.existsByEmail(signupDTO.getEmail());
        boolean usernameExists = userRepository.existsByUsername(signupDTO.getUsername());

        if (emailExists || usernameExists) {
            throw new RuntimeException("Email or Username already exists.");
        }

        Users user = new Users();
        user.setEmail(signupDTO.getEmail());
        user.setUsername(signupDTO.getUsername());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        userRepository.save(user);
    }

    public JwtTokenPairDTO loginUser(LoginDto loginDTO) {
        Users user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new JwtTokenPairDTO(accessToken, refreshToken);
    }

    public JwtTokenPairDTO refreshAccessToken(String refreshToken) {
        String email = jwtUtil.extractEmail(refreshToken);
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (!jwtUtil.validateToken(refreshToken, email)) {
            throw new RuntimeException("Refresh token expired or invalid");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        return new JwtTokenPairDTO(newAccessToken, newRefreshToken);
    }

    public UserDto updateUserProfile( String userId ,UserProfileUpdateDto userProfileUpdateDto){
            Users users= userRepository.findById(userId).orElseThrow(()-> new RuntimeException("user not found "));

            if (userProfileUpdateDto.getSkills()!=null){
                users.setSkills(userProfileUpdateDto.getSkills());
            } if (userProfileUpdateDto.getExperience()!=null){
                users.setExperience(userProfileUpdateDto.getExperience());
            } if (userProfileUpdateDto.getGithubLink()!=null){
                 users.setGithubLink(userProfileUpdateDto.getGithubLink());
            }if (userProfileUpdateDto.getDomain()!=null){
                users.setDomain(userProfileUpdateDto.getDomain());
            }if (userProfileUpdateDto.getBio()!=null){
                users.setBio(userProfileUpdateDto.getBio());
        }
        Users updated = userRepository.save(users);
            return modelMapper.map(updated,UserDto.class);
    }


    public List<UserDto> getAllUsers(){
        return userRepository.findAll()
                .stream().map(this::mapToUserDto)
                .collect(Collectors.toList());
    }
    private UserDto mapToUserDto(Users users){
            return modelMapper.map(users,UserDto.class);
    }




    // New method: Upload profile image and update user record
    public UserDto uploadProfileImage(String userId, MultipartFile imageFile) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Define the upload directory; for demo purposes, it is relative to the project root.
        String uploadDir = "uploads/";
        // Generate a unique file name using UUID to avoid collisions.
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        try {
            // Create the directory if it does not exist.
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Write the file to the upload directory.
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, imageFile.getBytes());
            // Build the URL to access this image. (For demo, we assume the server runs on localhost:8080.)
            String imageUrl = "http://localhost:8080/uploads/" + fileName;
            // Set the URL in the user entity.
            user.setProfileImageUrl(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile image", e);
        }

        Users updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class); // Return the updated user as DTO.
    }

}
