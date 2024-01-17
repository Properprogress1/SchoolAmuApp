package com.example.schoolmuapp.repositories;


import com.example.schoolmuapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<UserDetails> findByUsername(String username);

    //boolean existByUsername (String username);
    boolean existsByUsername(String username);

    User findByEmail(String username);
}
