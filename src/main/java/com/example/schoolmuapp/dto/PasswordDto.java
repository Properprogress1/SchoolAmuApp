package com.example.schoolmuapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordDto {
    private String email;
    private String newPassword;
    private String newConfirmPassword;
}
