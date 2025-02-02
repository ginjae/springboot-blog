package me.kimjaemin.springbootblog.dto;

import lombok.Getter;
import me.kimjaemin.springbootblog.domain.User;

@Getter
public class UserResponse {

    private final String nickname;
    private final String email;

    public UserResponse(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }

}
