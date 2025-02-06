package me.kimjaemin.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.config.error.exception.ArticleNotFoundException;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.dto.ArticleListViewResponse;
import me.kimjaemin.springbootblog.dto.ArticleViewResponse;
import me.kimjaemin.springbootblog.dto.CategoryResponse;
import me.kimjaemin.springbootblog.service.BlogService;
import me.kimjaemin.springbootblog.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;
    private final CategoryService categoryService;

    @GetMapping("/articles")
    public String getArticles(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(value = "type", defaultValue = "") String type,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @RequestParam(value = "category", defaultValue = "") String categoryName,
                              Model model) {
        Page<ArticleListViewResponse> page = blogService.getPage(pageable, type, keyword, categoryName)
                .map(ArticleListViewResponse::new);
        model.addAttribute("page", page);
        model.addAttribute("categories", categoryService.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList());

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model) {
         try {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        } catch (ArticleNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("categories", categoryService.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList());

        return "article";
    }

    @GetMapping("/write-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            try {
                Article article = blogService.findById(id);
                model.addAttribute("article", new ArticleViewResponse(article));
            } catch (ArticleNotFoundException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("article", new ArticleViewResponse());
            }
        }
        model.addAttribute("categories", categoryService.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList());

        return "writeArticle";
    }

}
