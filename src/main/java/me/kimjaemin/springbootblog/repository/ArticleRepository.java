package me.kimjaemin.springbootblog.repository;

import me.kimjaemin.springbootblog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("select a from Article a join fetch a.author")
    Page<Article> findAll(Pageable pageable);
}
