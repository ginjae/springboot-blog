package me.kimjaemin.springbootblog.controller;

import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @DisplayName("signup: 회원가입에 성공한다.")
    @Test
    public void signup() throws Exception {
        final String url = "/signup";
        final String email = "user@gmail.com";
        final String password = "12345678";
        final String nickname = "nickname";

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password)
                .param("nickname", nickname));

        User user = userRepository.findByEmail(email).get();
        assertThat(user).isNotNull();
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @DisplayName("logout: 로그아웃에 성공한다.")
    @Test
    public void logout() throws Exception {
        final String url = "/logout";
        final String email = "user@gmail.com";
        final String password = "12345678";
        final String nickname = "nickname";
        User user = userRepository.save(User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo(email);

        mockMvc.perform(get(url));
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

}