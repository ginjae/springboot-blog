package me.kimjaemin.springbootblog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.config.error.exception.ArticleNotFoundException;
import me.kimjaemin.springbootblog.config.error.exception.UnauthorizedException;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.domain.Comment;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddArticleRequest;
import me.kimjaemin.springbootblog.dto.AddCommentRequest;
import me.kimjaemin.springbootblog.dto.UpdateArticleRequest;
import me.kimjaemin.springbootblog.repository.BlogRepository;
import me.kimjaemin.springbootblog.repository.CommentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    public Article save(AddArticleRequest request, User author) {
        return blogRepository.save(request.toEntity(author));
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);

        authorizeArticleAuthor(article);
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update (long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    private static void authorizeArticleAuthor(Article article) {
        Long id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        if (!article.getAuthor().getId().equals(id)) {
            throw new UnauthorizedException();
        }
    }

    public Comment addComment(AddCommentRequest request, User author) {
        Article article = blogRepository.findById(request.getArticleId())
                .orElseThrow(ArticleNotFoundException::new);

        return commentRepository.save(request.toEntity(article, author));
    }

}
