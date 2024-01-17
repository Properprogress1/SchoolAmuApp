package com.example.schoolmuapp.models;

import com.example.schoolmuapp.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user-seq", initialValue = 2000)
    @JsonIgnore

    private Long id;
    @Column(unique = true)
    //private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;

//    @JsonIgnore
//    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
//    private List<Post> posts;
//    @Enumerated(value = EnumType.STRING)
//    private Role userRole;


    @JsonIgnore
    public boolean isAccountNotExpired(){return true;}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked(){return true;}

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired(){return true;}

    @Override
    @JsonIgnore
    public boolean isEnabled(){return true;}

    public void setUserRole(Role role) {
    }
}
