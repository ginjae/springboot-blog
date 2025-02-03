package me.kimjaemin.springbootblog.dto;

import lombok.Getter;
import me.kimjaemin.springbootblog.domain.Comment;

@Getter
public class CommentResponse {

    private final String author;
    private final String articleTitle;
    private final String content;

    public CommentResponse(Comment comment) {
        this.author = comment.getAuthor().getNickname();
        this.articleTitle = comment.getArticle().getTitle();
        this.content = comment.getContent();
    }

}
