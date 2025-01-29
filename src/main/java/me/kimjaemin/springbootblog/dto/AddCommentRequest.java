package me.kimjaemin.springbootblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.domain.Comment;
import me.kimjaemin.springbootblog.domain.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequest {

    @NotNull
    private Long articleId;

    @NotBlank(message = "댓글을 입력해주세요.")
    @Size(min = 1, max = 30, message = "댓글은 1-200자로 입력해주세요.")
    private String content;

    public Comment toEntity(Article article, User author) {
        return Comment.builder()
                .article(article)
                .author(author)
                .content(content)
                .build();
    }

}
