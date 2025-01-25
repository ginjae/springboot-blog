package me.kimjaemin.springbootblog.service;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddUserRequest;
import me.kimjaemin.springbootblog.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest addUserRequest) {
        if (userRepository.findByEmail(addUserRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Duplicated email");
        }

        return userRepository.save(User.builder()
                .email(addUserRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(addUserRequest.getPassword1()))
                .nickname(addUserRequest.getNickname())
                .build()).getId();
    }

}
