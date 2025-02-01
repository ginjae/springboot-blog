package me.kimjaemin.springbootblog.repository;

import me.kimjaemin.springbootblog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Specification<Article> specification, Pageable pageable);
}
