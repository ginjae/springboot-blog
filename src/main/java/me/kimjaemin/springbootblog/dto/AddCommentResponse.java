package me.kimjaemin.springbootblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimjaemin.springbootblog.domain.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentResponse {

    private Long id;
    private String content;

    public AddCommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
    }

}
