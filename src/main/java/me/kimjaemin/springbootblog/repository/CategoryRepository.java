package me.kimjaemin.springbootblog.repository;

import me.kimjaemin.springbootblog.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);

    boolean existsByName(String name);
}
