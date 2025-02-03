package me.kimjaemin.springbootblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {

    @NotBlank(message = "카테고리를 입력해 주세요.")
    private String categoryName;

    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(min = 1, max = 30, message = "제목은 1-30자로 입력해 주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

}
