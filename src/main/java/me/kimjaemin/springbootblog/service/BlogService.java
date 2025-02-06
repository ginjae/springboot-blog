package me.kimjaemin.springbootblog.service;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.config.error.exception.ArticleNotFoundException;
import me.kimjaemin.springbootblog.config.error.exception.CommentNotFoundException;
import me.kimjaemin.springbootblog.config.error.exception.UnauthorizedException;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.domain.Category;
import me.kimjaemin.springbootblog.domain.Comment;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddArticleRequest;
import me.kimjaemin.springbootblog.dto.AddCommentRequest;
import me.kimjaemin.springbootblog.dto.UpdateArticleRequest;
import me.kimjaemin.springbootblog.repository.ArticleRepository;
import me.kimjaemin.springbootblog.repository.CategoryRepository;
import me.kimjaemin.springbootblog.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public Article save(AddArticleRequest request, User author) {
        Article article = request.toEntity(author, categoryService);
        if (article.getCategory().getAllowedRole().equals("ROLE_ADMIN")) {
            authorizeAdmin();
        }
        return articleRepository.save(article);
    }

    public Specification<Article> search(String type, String keyword, String categoryName) {
        if (type.equals("title")) {
            return new Specification<Article>() {
                private static final long serialVersionUID = 1L;
                @Override
                public Predicate toPredicate(Root<Article> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query.distinct(true);
                    Predicate categoryPredicate = (categoryName != null) ?
                            cb.equal(a.join("category", JoinType.INNER).get("name"), categoryName) : cb.conjunction();

                    return cb.and(categoryPredicate,
                            cb.like(cb.lower(a.get("title")), "%" + keyword.toLowerCase() + "%"));
                }
            };
        } else if (type.equals("author")) {
            return new Specification<Article>() {
                private static final long serialVersionUID = 1L;
                @Override
                public Predicate toPredicate(Root<Article> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query.distinct(true);
                    Join<Article, User> userJoin = a.join("author", JoinType.INNER);
                    Predicate categoryPredicate = (categoryName != null) ?
                            cb.equal(a.join("category", JoinType.INNER).get("name"), categoryName) : cb.conjunction();
                    return cb.and(categoryPredicate,
                            cb.like(cb.lower(userJoin.get("nickname")), "%" + keyword.toLowerCase() + "%"));
                }
            };
        }
        return new Specification<Article>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Article> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Predicate categoryPredicate = (categoryName != null) ?
                        cb.equal(a.join("category", JoinType.INNER).get("name"), categoryName) : cb.conjunction();
                return cb.and(categoryPredicate,
                        cb.or(cb.like(cb.lower(a.get("title")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(a.get("content")), "%" + keyword.toLowerCase() + "%")));
            }
        };
    }

    public Page<Article> getPage(Pageable pageable, String type, String keyword, String categoryName) {
        if (!categoryRepository.existsByName(categoryName)) {
            Specification<Article> specification = search(type, keyword, null);
            return articleRepository.findAll(specification, pageable);
        } else {
            Specification<Article> specification = search(type, keyword, categoryName);
            return articleRepository.findAll(specification, pageable);
        }
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
        Category category = categoryService.getCategoryByName(request.getCategoryName());
        if (category.getAllowedRole().equals("ROLE_ADMIN")) {
            authorizeAdmin();
        }

        authorizeArticleAuthor(article);
        article.update(category, request.getTitle(), request.getContent());

        return article;
    }

    private static void authorizeAdmin() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new UnauthorizedException();
        }
    }

    private static void authorizeArticleAuthor(Article article) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return;
        }
        Long id = user.getId();
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = user.getId();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return;
        }
        if (!comment.getAuthor().getId().equals(id)) {
            throw new UnauthorizedException();
        }
    }

}
