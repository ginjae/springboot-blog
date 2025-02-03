package me.kimjaemin.springbootblog.service;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddUserRequest;
import me.kimjaemin.springbootblog.dto.UpdateUserRequest;
import me.kimjaemin.springbootblog.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User save(AddUserRequest addUserRequest) {
        if (!addUserRequest.getPassword1().equals(addUserRequest.getPassword2())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.existsByEmail(addUserRequest.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일 입니다.");
        }
        if (userRepository.existsByNickname(addUserRequest.getNickname())) {
            throw new IllegalArgumentException("이미 등록된 닉네임 입니다.");
        }

        return userRepository.save(User.builder()
                .email(addUserRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(addUserRequest.getPassword1()))
                .nickname(addUserRequest.getNickname())
                .build());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User update(UpdateUserRequest updateUserRequest, User user) {
        if (userRepository.existsByNickname(updateUserRequest.getNickname())) {
            throw new IllegalArgumentException("이미 등록된 닉네임 입니다.");
        }

        user.update(updateUserRequest.getNickname());

        return userRepository.save(user);
    }

}
