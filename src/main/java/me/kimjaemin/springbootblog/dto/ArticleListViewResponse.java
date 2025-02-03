package me.kimjaemin.springbootblog.dto;

import lombok.Getter;
import me.kimjaemin.springbootblog.domain.Article;

import java.time.LocalDateTime;

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String author;
    private final String category;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final int commentCount;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.author = article.getAuthor().getNickname();
        this.category = article.getCategory().getName();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.commentCount = article.getComments().size();
    }

}
