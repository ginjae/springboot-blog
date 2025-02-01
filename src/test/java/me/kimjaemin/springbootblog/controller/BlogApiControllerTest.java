package me.kimjaemin.springbootblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kimjaemin.springbootblog.config.error.ErrorCode;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.domain.Comment;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddArticleRequest;
import me.kimjaemin.springbootblog.dto.AddCommentRequest;
import me.kimjaemin.springbootblog.dto.UpdateArticleRequest;
import me.kimjaemin.springbootblog.repository.ArticleRepository;
import me.kimjaemin.springbootblog.repository.CommentRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    User user;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        articleRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("12345678")
                .nickname("nickname")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());
        List<Article> articles = articleRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getAuthor().getNickname()).isEqualTo(user.getNickname());
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("addArticle: 블로그 글 추가할 때 title이 null이면 실패한다.")
    @Test
    public void addArticleNullValidation() throws Exception {
        final String url = "/api/articles";
        final String title = null;
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isBadRequest());
    }

    @DisplayName("addArticle: 블로그 글 추가할 때 title이 30자를 넘으면 실패한다.")
    @Test
    public void addArticleSizeValidation() throws Exception {
        final String url = "/api/articles";
        final String title = "t".repeat(31);
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isBadRequest());
    }

    @DisplayName("addArticle: 블로그 글 추가할 때 content가 256자를 넘어도 성공한다.")
    @Test
    public void addArticleContentValidation() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "c".repeat(257);
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());
    }

    @DisplayName("findArticles: 블로그 글 1페이지 목록 조회에 성공한다.")
    @Test
    public void findArticles() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        articleRepository.save(Article.builder()
                .author(user)
                .title(title)
                .content(content)
                .build());

        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(title))
                .andExpect(jsonPath("$.content[0].content").value(content));
    }

    @DisplayName("findArticlesByTitle: 블로그 글 title로 검색에 성공한다.")
    @Test
    public void findArticlesByTitle() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        articleRepository.save(Article.builder()
                .author(user)
                .title(title)
                .content(content)
                .build());
        articleRepository.save(Article.builder()
                .author(user)
                .title(title.toUpperCase())
                .content(content.toUpperCase())
                .build());

        final ResultActions result = mockMvc.perform(get(url)
                .param("type", "title")
                .param("keyword", "title")
                .accept(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value(title))
                .andExpect(jsonPath("$.content[0].content").value(content));
    }

    @DisplayName("findArticlesByContent: 블로그 글 content로 검색에 성공한다.")
    @Test
    public void findArticlesByContent() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        articleRepository.save(Article.builder()
                .author(user)
                .title(title)
                .content(content)
                .build());
        articleRepository.save(Article.builder()
                .author(user)
                .title(title.toUpperCase())
                .content(content.toUpperCase())
                .build());

        final ResultActions result = mockMvc.perform(get(url)
                .param("type", "content")
                .param("keyword", "content")
                .accept(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value(title))
                .andExpect(jsonPath("$.content[0].content").value(content));
    }

    @DisplayName("findArticlesByAuthor: 블로그 글 author로 검색에 성공한다.")
    @Test
    public void findArticlesByAuthor() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        articleRepository.save(Article.builder()
                .author(user)
                .title(title)
                .content(content)
                .build());
        User otherUser = userRepository.save(User.builder()
                .email("user2@gmail.com")
                .password("12345678")
                .nickname("NICKNAME")
                .build());
        articleRepository.save(Article.builder()
                .author(otherUser)
                .title(title.toUpperCase())
                .content(content.toUpperCase())
                .build());

        final ResultActions result = mockMvc.perform(get(url)
                .param("type", "author")
                .param("keyword", "nickname")
                .accept(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value(title))
                .andExpect(jsonPath("$.content[0].content").value(content));
    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        Article savedArticle = articleRepository.save(Article.builder()
                .author(user)
                .title(title)
                .content(content)
                .build());

        final ResultActions result = mockMvc.perform(get(url, savedArticle.getId()));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));
    }

    @DisplayName("findArticle: 잘못된 HTTP 메서드로의 블로그 글 조회에 실패한다.")
    @Test
    public void findArticleInvalidHttpMethod() throws Exception {
        final String url = "/api/articles/{id}";

        final ResultActions result = mockMvc.perform(post(url, 1));

        result
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
    }

    @DisplayName("findArticle: 존재하지 않는 블로그 글 조회에 실패한다.")
    @Test
    public void findArticleInvalidId() throws Exception {
        final String url = "/api/articles/{id}";
        final long invalidId = 1;

        final ResultActions result = mockMvc.perform(get(url, invalidId));

        result
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.ARTICLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.getCode()));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        Article savedArticle = articleRepository.save(Article.builder()
                .author(user)
                .title(title)
                .content(content)
                .build());

        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        List<Article> articles = articleRepository.findAll();
        assertThat(articles).isEmpty();
    }

    @DisplayName("deleteArticle: 존재하지 않는 블로그 글 삭제에 실패한다.")
    @Test
    public void deleteArticleInvalidId() throws Exception {
        final String url = "/api/articles/{id}";
        final long invalidId = 1;

        final ResultActions result = mockMvc.perform(delete(url, invalidId));

        result
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.ARTICLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.getCode()));
    }

    @DisplayName("deleteArticle: 권한이 없는 블로그 글 삭제에 실패한다.")
    @Test
    public void deleteArticleUnauthorized() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        User otherUser = userRepository.save(User.builder()
                .email("user2@gmail.com")
                .password("12345678")
                .nickname("nickname2")
                .build());
        Article savedArticle = articleRepository.save(Article.builder()
                .author(otherUser)
                .title(title)
                .content(content)
                .build());

        final ResultActions result = mockMvc.perform(delete(url, savedArticle.getId()));

        result
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(ErrorCode.FORBIDDEN.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.FORBIDDEN.getCode()));
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        Article savedArticle = articleRepository.save(Article.builder()
                .author(user)
                .title(title)
                .content(content)
                .build());
        final String newTitle = "new title";
        final String newContent = "new content";
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk());
        Article article = articleRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    @DisplayName("updateArticle: 권한이 없는 블로그 글 수정에 실패한다.")
    @Test
    public void updateArticleUnauthorized() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        User otherUser = userRepository.save(User.builder()
                .email("user2@gmail.com")
                .password("12345678")
                .nickname("nickname2")
                .build());
        Article savedArticle = articleRepository.save(Article.builder()
                .author(otherUser)
                .title(title)
                .content(content)
                .build());
        final String newTitle = "new title";
        final String newContent = "new content";
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        result
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(ErrorCode.FORBIDDEN.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.FORBIDDEN.getCode()));
    }

    @DisplayName("addComment: 다른 사람이 쓴 글에 댓글 추가에 성공한다.")
    @Test
    public void addComment() throws Exception {
        final String url = "/api/comments";
        final String title = "title";
        final String content = "content";
        User otherUser = userRepository.save(User.builder()
                .email("user2@gmail.com")
                .password("12345678")
                .nickname("nickname2")
                .build());
        Article savedArticle = articleRepository.save(Article.builder()
                .author(otherUser)
                .title(title)
                .content(content)
                .build());
        final Long articleId = savedArticle.getId();
        final String comment = "comment";
        final AddCommentRequest request = new AddCommentRequest(articleId, comment);
        final String requestBody = objectMapper.writeValueAsString(request);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getArticle().getId()).isEqualTo(articleId);
        assertThat(comments.get(0).getContent()).isEqualTo(comment);
    }

    @DisplayName("deleteComment: 댓글 삭제에 성공한다.")
    @Test
    public void deleteComment() throws Exception {
        final String url = "/api/comments/{id}";
        final String title = "title";
        final String content = "content";
        User otherUser = userRepository.save(User.builder()
                .email("user2@gmail.com")
                .password("12345678")
                .nickname("nickname2")
                .build());
        Article savedArticle = articleRepository.save(Article.builder()
                .author(otherUser)
                .title(title)
                .content(content)
                .build());
        Comment savedComment = commentRepository.save(Comment.builder()
                .article(savedArticle)
                .author(user)
                .content("comment")
                .build());

        mockMvc.perform(delete(url, savedComment.getId()))
                .andExpect(status().isOk());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(0);
    }

}