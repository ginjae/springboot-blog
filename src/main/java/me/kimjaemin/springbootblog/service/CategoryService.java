package me.kimjaemin.springbootblog.service;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.config.error.exception.CategoryNotFoundException;
import me.kimjaemin.springbootblog.config.error.exception.UnauthorizedException;
import me.kimjaemin.springbootblog.domain.Category;
import me.kimjaemin.springbootblog.domain.User;
import me.kimjaemin.springbootblog.dto.AddCategoryRequest;
import me.kimjaemin.springbootblog.repository.CategoryRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(AddCategoryRequest addCategoryRequest) {
        authorizeAdmin();
        return categoryRepository.save(addCategoryRequest.toEntity());
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(CategoryNotFoundException::new);
    }

    public void deleteCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(CategoryNotFoundException::new);

        authorizeAdmin();
        categoryRepository.delete(category);
    }

    private static void authorizeAdmin() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new UnauthorizedException();
        }
    }

}
