package me.kimjaemin.springbootblog.repository;

import me.kimjaemin.springbootblog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
