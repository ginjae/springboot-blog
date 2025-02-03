package me.kimjaemin.springbootblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kimjaemin.springbootblog.domain.Category;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddCategoryRequest;
import me.kimjaemin.springbootblog.repository.ArticleRepository;
import me.kimjaemin.springbootblog.repository.CategoryRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    User user;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build();
        categoryRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("12345678")
                .nickname("user")
                .role("ROLE_ADMIN")
                .build());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    @DisplayName("addCategory: 카테고리 추가에 성공한다.")
    @Test
    public void addCategory() throws Exception {
        final String url = "/api/categories";
        final String name = "category";
        final AddCategoryRequest addCategoryRequest = new AddCategoryRequest(name);
        final String requestBody = objectMapper.writeValueAsString(addCategoryRequest);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getName()).isEqualTo(name);
    }

    @DisplayName("deleteCategory: 카테고리 삭제에 성공한다.")
    @Test
    public void deleteCategory() throws Exception {
        final String url = "/api/categories/{name}";
        final String name = "category";
        Category savedcategory = categoryRepository.save(Category.builder()
                .name(name)
                .build());

        mockMvc.perform(delete(url, name))
                .andExpect(status().isOk());

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).isEmpty();

    }

}