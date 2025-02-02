package me.kimjaemin.springbootblog.config;

import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@blog.com").isEmpty()) {
                User admin = User.builder()
                        .email("admin@admin")
                        .password(bCryptPasswordEncoder.encode("11111111"))
                        .nickname("관리자")
                        .role("ROLE_ADMIN")
                        .build();

                userRepository.save(admin);
            }
        };
    }

}
