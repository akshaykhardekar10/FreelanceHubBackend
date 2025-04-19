package com.example.FreelanceX2.Service;

import com.example.FreelanceX2.Config.JwtUtil;
import com.example.FreelanceX2.DTO.*;

import com.example.FreelanceX2.Model.Users;
import com.example.FreelanceX2.Repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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


}
