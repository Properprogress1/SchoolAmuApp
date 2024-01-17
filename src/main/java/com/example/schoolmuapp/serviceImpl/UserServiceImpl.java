package com.example.schoolmuapp.serviceImpl;

import com.example.schoolmuapp.dto.UserDto;
import com.example.schoolmuapp.exception.PasswordNotFoundException;
import com.example.schoolmuapp.models.PasswordResetToken;
import com.example.schoolmuapp.models.User;
import com.example.schoolmuapp.repositories.PasswordResetTokenRepository;
import com.example.schoolmuapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static com.example.schoolmuapp.enums.Role.ROLE_USER;

@Service
public class UserServiceImpl implements UserDetailsService {


    private PasswordResetTokenRepository passwordResetTokenRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl( PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Function<UserDto, User> saveUser= (userDto)->{
        User user = new ObjectMapper().convertValue(userDto, User.class);
        user.setPassword (passwordEncoder.encode(user.getPassword()));
        user.setUserRole(ROLE_USER);
        user.setFirstName(user.getFirstName());
        user.setLastName(userDto.getLastName());
        return userRepository.save(user);
    };

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found with username: "+username));
    }


    //to generate 2FAtoken 6 digits token
    public String generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generates a random digit between 0 and 9
            stringBuilder.append(digit);
        }

        return stringBuilder.toString();
    }

    //call the createPasswordResetTokenForUser

    public void savePasswordResetTokenForUser(User user, String token){
        PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public User findUserByEmail(String username){
        return userRepository.findByEmail(username);

    }


    public String validatePasswordResetToken(String token, PasswordDto passwordDto) {
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null){
            return "invalid";
        }
        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();
        //password reset token time upon creation is 10min
        //system time(cal) upon password reset token creation starts from 0min
        //so (10-0 is 10min), when it gets to (10min - 10mins(system time) it becomes 0)
        //then it means the password reset token is expired, then delete token from database and return expired
        if(passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime() <= 0){
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        //after checking the token is not invalid(null) and the token is not yet expired
        //then return valid
        return "valid";
    }

    public Optional<User> getUserByPasswordReset(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    public void changePassword(User user, String newPassword, String newConfirmPassword) {
        if(newPassword.equals(newConfirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setConfirmPassword(passwordEncoder.encode(newConfirmPassword));
            userRepository.save(user);
        }else {
            throw new PasswordNotFoundException("Passwords do not match");

        }
    }

}