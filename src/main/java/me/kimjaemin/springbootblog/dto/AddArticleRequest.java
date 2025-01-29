package me.kimjaemin.springbootblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.domain.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddArticleRequest {

    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(min = 1, max = 30, message = "제목은 1-30자로 입력해 주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    public Article toEntity(User author) {
        return Article.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();
    }

}
