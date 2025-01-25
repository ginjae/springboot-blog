package me.kimjaemin.springbootblog.controller;

import me.kimjaemin.springbootblog.dto.AddUserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(AddUserRequest addUserRequest) {
        return "signup";
    }

}
