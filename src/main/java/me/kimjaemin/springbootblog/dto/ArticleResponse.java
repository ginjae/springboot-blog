package me.kimjaemin.springbootblog.dto;

import lombok.Getter;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArticleResponse {

    private final Long id;
    private final String author;
    private final String category;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<Comment> comments;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.author = article.getAuthor().getUsername();
        this.category = article.getCategory().getName();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.comments = article.getComments();
    }

}
