package com.DateBuzz.Backend.model;

import com.DateBuzz.Backend.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class User implements UserDetails {

    private Long id;
    private String userName;
    private String password;
    private String profileImg;
    private String nickname;
    private String email;

    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity user) {
        return new User(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getProfileImg(),
                user.getNickname(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}