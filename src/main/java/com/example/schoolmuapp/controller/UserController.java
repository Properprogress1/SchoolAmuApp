package com.example.schoolmuapp.controller;


import com.example.schoolmuapp.dto.PasswordDto;
import com.example.schoolmuapp.exception.UserNotFoundException;
import com.example.schoolmuapp.models.User;
import com.example.schoolmuapp.serviceImpl.EmailSenderService;
import com.example.schoolmuapp.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserServiceImpl userService;
    private EmailSenderService emailService;

    @Autowired
    public UserController(EmailSenderService emailService, UserServiceImpl userService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    //PasswordDto will collect only email
//    @PostMapping("/resetPassword")
//    public ResponseEntity<String> resetPassword(@RequestBody PasswordDto passwordDto,
//                                                HttpServletRequest request) {
//        org.springframework.security.core.userdetails.User user = userService.findUserByEmail(passwordDto.getEmail());
//        String url = "";
//        if(user != null){
//            String twoFAToken =  userService.generateRandomNumber(6);
//            userService.savePasswordResetTokenForUser(user, twoFAToken);
//            url = emailService.passwordResetTokenMail(user, emailService.applicationUrl(request), twoFAToken);
//            return new ResponseEntity<>(url, HttpStatus.OK);
//        }
//        throw new UserNotFoundException("User with email " + passwordDto.getEmail() + " not Found!");
//    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordDto passwordDto,
                                                HttpServletRequest request){
        User user = userService.findUserByEmail(passwordDto.getEmail());
        String url = "";
        if (user != null){
            String twoFAToken = userService.generateRandomNumber(6);
            userService.savePasswordResetTokenForUser(user,twoFAToken);
            url = emailService.passwordResetTokenMail(user, emailService.applicationUrl(request), twoFAToken);
            return new ResponseEntity<>(url, HttpStatus.OK);
        }
        throw new UserNotFoundException("User with email " + passwordDto.getEmail() + " not Found!");

        }



    //To save Password, I need to use both the new Password and new Confirm Password to ask to save password in PasswordDto
    @PostMapping("/savePassword")
    public ResponseEntity<String> savePassword(@RequestParam("token") String token,
                                               @RequestBody PasswordDto passwordDto) throws BadRequestException {
        String result = userService.validatePasswordResetToken(token, passwordDto);
        if(!result.equalsIgnoreCase("valid")){
            return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }

        Optional<com.example.schoolmuapp.models.User> user = userService.getUserByPasswordReset(token);
        if(user.isPresent()){
            userService.changePassword(user.get(), passwordDto.getNewPassword(), passwordDto.getNewConfirmPassword());
            return new ResponseEntity<>("Password Reset Successful", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }
    }
}