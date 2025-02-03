package me.kimjaemin.springbootblog.dto;

import lombok.Getter;
import me.kimjaemin.springbootblog.domain.User;

@Getter
public class UserResponse {

    private final String email;
    private final String nickname;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }

}
