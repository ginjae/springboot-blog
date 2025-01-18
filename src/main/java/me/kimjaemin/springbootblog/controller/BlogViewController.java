package me.kimjaemin.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.dto.ArticleListViewResponse;
import me.kimjaemin.springbootblog.dto.ArticleViewResponse;
import me.kimjaemin.springbootblog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticle(@RequestParam(required = false) String sort, Model model) {
        List<ArticleListViewResponse> articles = new java.util.ArrayList<>(blogService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList());
        Comparator<ArticleListViewResponse> comparator = (a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt());
        if (sort != null && sort.equals("oldest")) {
            articles.sort(comparator);
        } else {
            articles.sort(comparator.reversed());
        }
        model.addAttribute("articles", articles);

        return "articleList";
    }


    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }

}
