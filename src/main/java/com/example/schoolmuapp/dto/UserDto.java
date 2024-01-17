package com.example.schoolmuapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String userRole;
}
