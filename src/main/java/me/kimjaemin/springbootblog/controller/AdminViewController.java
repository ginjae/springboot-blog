package me.kimjaemin.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.dto.CategoryResponse;
import me.kimjaemin.springbootblog.dto.UserResponse;
import me.kimjaemin.springbootblog.repository.CategoryRepository;
import me.kimjaemin.springbootblog.repository.UserRepository;
import me.kimjaemin.springbootblog.service.CategoryService;
import me.kimjaemin.springbootblog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AdminViewController {

    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.findAll()
                .stream()
                .map(UserResponse::new)
                .toList());
        model.addAttribute("categories", categoryService.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList());

        return "admin";
    }

}
