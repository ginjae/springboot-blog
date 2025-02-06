package me.kimjaemin.springbootblog.dto;

import lombok.Getter;
import me.kimjaemin.springbootblog.domain.User;

import java.time.LocalDateTime;

@Getter
public class UserResponse {

    private final String email;
    private final String nickname;
    private final String role;
    private final LocalDateTime createdAt;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }

}
