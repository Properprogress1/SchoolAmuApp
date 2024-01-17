package com.example.schoolmuapp.repositories;

import com.example.schoolmuapp.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {


    PasswordResetToken findByToken(String token);
}
