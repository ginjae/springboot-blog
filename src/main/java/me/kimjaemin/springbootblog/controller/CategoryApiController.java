package me.kimjaemin.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.domain.Category;
import me.kimjaemin.springbootblog.dto.AddCategoryRequest;
import me.kimjaemin.springbootblog.dto.CategoryResponse;
import me.kimjaemin.springbootblog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping("/api/categories")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody @Validated AddCategoryRequest addCategoryRequest) {
        Category savedCategory = categoryService.save(addCategoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryResponse(savedCategory));
    }

    @DeleteMapping("/api/categories/{name}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable("name") String name) {
        categoryService.deleteCategoryByName(name);

        return ResponseEntity.ok()
                .build();
    }

}
