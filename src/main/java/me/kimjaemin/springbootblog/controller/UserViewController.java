package me.kimjaemin.springbootblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.UserResponse;
import me.kimjaemin.springbootblog.service.BlogService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    private final BlogService blogService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if (referer != null && !referer.contains("/signup") && !referer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referer);
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if (referer != null && !referer.contains("/signup") && !referer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referer);
        }
        return "signup";
    }

    @GetMapping("/userinfo")
    public String userinfo(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", new UserResponse(user));

        return "userinfo";
    }

}
