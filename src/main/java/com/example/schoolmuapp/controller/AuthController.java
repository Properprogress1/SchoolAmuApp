package com.example.schoolmuapp.controller;

import com.example.schoolmuapp.dto.UserDto;
import com.example.schoolmuapp.serviceImpl.UserServiceImpl;
import com.example.schoolmuapp.utils.GoogleJwtUtils;
import com.example.schoolmuapp.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AuthController {

    private UserServiceImpl userService;

    private GoogleJwtUtils googleJwtUtils;

    private JwtUtils jwtUtils;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserServiceImpl userService, JwtUtils jwtUtils, GoogleJwtUtils googleJwtUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.googleJwtUtils = googleJwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/index")
    @SecurityRequirement(name = "Bearer Authentication")
    public String index() {
        return "index";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUpUser(@RequestBody UserDto userDto) {
        User user = userService.saveUser.apply(userDto);
        UserDto userDto1 = new ObjectMapper().convertValue(user, UserDto.class);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        UserDetails user = userService.loadUserByUsername(userDto.getUsername());
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            //String token = jwtUtils.createJwt.apply(user);
            String token = jwtUtils.createJwt.apply(user);
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("userName is not correct", HttpStatus.BAD_REQUEST);
    }
}
