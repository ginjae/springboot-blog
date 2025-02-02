package me.kimjaemin.springbootblog.service;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.config.error.exception.ArticleNotFoundException;
import me.kimjaemin.springbootblog.config.error.exception.CommentNotFoundException;
import me.kimjaemin.springbootblog.config.error.exception.UnauthorizedException;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.domain.Comment;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddArticleRequest;
import me.kimjaemin.springbootblog.dto.AddCommentRequest;
import me.kimjaemin.springbootblog.dto.UpdateArticleRequest;
import me.kimjaemin.springbootblog.repository.ArticleRepository;
import me.kimjaemin.springbootblog.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public Article save(AddArticleRequest request, User author) {
        return articleRepository.save(request.toEntity(author));
    }

    public Specification<Article> search(String type, String keyword) {
        if (type.equals("title")) {
            return new Specification<Article>() {
                private static final long serialVersionUID = 1L;
                @Override
                public Predicate toPredicate(Root<Article> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query.distinct(true);
                    return cb.like(a.get("title"), "%" + keyword + "%");
                }
            };
        } else if (type.equals("author")) {
            return new Specification<Article>() {
                private static final long serialVersionUID = 1L;
                @Override
                public Predicate toPredicate(Root<Article> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query.distinct(true);
                    Join<Article, User> userJoin = a.join("author", JoinType.LEFT);
                    return cb.like(userJoin.get("nickname"), "%" + keyword + "%");
                }
            };
        }
        return new Specification<Article>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Article> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                return cb.or(cb.like(a.get("title"), "%" + keyword + "%"),
                        cb.like(a.get("content"), "%" + keyword + "%"));
            }
        };
    }

    public Page<Article> getPage(Pageable pageable, String type, String keyword) {
        Specification<Article> specification = search(type, keyword);
        return articleRepository.findAll(specification, pageable);
    }

    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    public void delete(long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);

        authorizeArticleAuthor(article);
        articleRepository.deleteById(id);
    }

    @Transactional
    public Article update (long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
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
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(ArticleNotFoundException::new);

        return commentRepository.save(request.toEntity(article, author));
    }

    public void deleteComment(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);

        authorizeCommentAuthor(comment);
        commentRepository.deleteById(id);
    }

    private static void authorizeCommentAuthor(Comment comment) {
        Long id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        if (!comment.getAuthor().getId().equals(id)) {
            throw new UnauthorizedException();
        }
    }

}
