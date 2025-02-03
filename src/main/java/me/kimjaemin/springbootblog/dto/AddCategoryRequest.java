package me.kimjaemin.springbootblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimjaemin.springbootblog.domain.Category;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryRequest {

    @NotBlank(message = "카테고리 이름을 입력해 주세요.")
    @Size(min = 1, max = 10, message = "카테고리 이름은 1-10자로 입력해 주세요.")
    private String name;

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .build();
    }

}
