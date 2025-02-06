package me.kimjaemin.springbootblog.dto;

import lombok.Getter;
import me.kimjaemin.springbootblog.domain.Category;

import java.time.LocalDateTime;

@Getter
public class CategoryResponse {

    private final String name;
    private final int articleCount;
    private final LocalDateTime createdAt;
    private final String allowedRole;

    public CategoryResponse(Category category) {
        this.name = category.getName();
        this.articleCount = category.getArticles().size();
        this.createdAt = category.getCreatedAt();
        this.allowedRole = category.getAllowedRole();
    }

}
