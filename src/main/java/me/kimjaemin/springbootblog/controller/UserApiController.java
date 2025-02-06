package me.kimjaemin.springbootblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddUserRequest;
import me.kimjaemin.springbootblog.dto.UpdateUserRequest;
import me.kimjaemin.springbootblog.dto.UserResponse;
import me.kimjaemin.springbootblog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody @Validated AddUserRequest addUserRequest, Model model) {
        User user = userService.save(addUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(user));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/userinfo")
    public ResponseEntity<UserResponse> updateNickname(@RequestBody @Validated UpdateUserRequest updateUserRequest,
                                                       @AuthenticationPrincipal User user) {
        User updatedUser = userService.update(updateUserRequest, user);

        return ResponseEntity.ok()
                .body(new UserResponse(updatedUser));
    }

    @PutMapping("/userinfo/{email}")
    public ResponseEntity<UserResponse> toAdmin(@PathVariable String email) {
        User admin = userService.toAdmin(email);

        return ResponseEntity.ok()
                .body(new UserResponse(admin));
    }
}
