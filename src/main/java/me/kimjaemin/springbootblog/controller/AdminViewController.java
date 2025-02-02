package me.kimjaemin.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.dto.UserResponse;
import me.kimjaemin.springbootblog.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AdminViewController {

    private final UserRepository userRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll().stream().map(UserResponse::new).toList());
        return "admin";
    }

}
