package me.kimjaemin.springbootblog.repository;

import me.kimjaemin.springbootblog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
