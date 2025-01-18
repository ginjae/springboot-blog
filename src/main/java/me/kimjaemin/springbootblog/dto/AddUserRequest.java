package me.kimjaemin.springbootblog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String username;
    private String email;
    private String password;
}
