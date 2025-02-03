package me.kimjaemin.springbootblog.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import me.kimjaemin.springbootblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
