package me.kimjaemin.springbootblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import me.kimjaemin.springbootblog.dto.AddUserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if (referer != null && !referer.contains("/signup") && !referer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referer);
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, AddUserRequest addUserRequest) {
        String referer = request.getHeader("referer");
        if (referer != null && !referer.contains("/signup") && !referer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referer);
        }
        return "signup";
    }

}
