package me.kimjaemin.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.domain.Comment;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.*;
import me.kimjaemin.springbootblog.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody @Validated AddArticleRequest addArticleRequest,
                                                      @AuthenticationPrincipal User user) {
        Article savedArticle = blogService.save(addArticleRequest, user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(savedArticle));
    }

    @GetMapping("/api/articles")
    public ResponseEntity<Page<ArticleResponse>> findArticles(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                              @RequestParam(value = "type", defaultValue = "") String type,
                                                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                              @RequestParam(value = "category", defaultValue = "") String categoryName) {
        Page<ArticleResponse> page = blogService.getPage(pageable, type, keyword, categoryName)
                .map(ArticleResponse::new);

        return ResponseEntity.ok()
                .body(page);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticleById(@PathVariable("id") Long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable("id") Long id,
                                                         @RequestBody @Validated UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(new ArticleResponse(updatedArticle));
    }

    @PostMapping("/api/comments")
    public ResponseEntity<CommentResponse> addComment(@RequestBody @Validated AddCommentRequest addCommentRequest,
                                                      @AuthenticationPrincipal User user) {
        Comment savedComment = blogService.addComment(addCommentRequest, user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommentResponse(savedComment));
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        blogService.deleteComment(id);

        return ResponseEntity.ok()
                .build();
    }

}
