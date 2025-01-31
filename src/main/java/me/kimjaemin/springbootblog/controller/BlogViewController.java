package me.kimjaemin.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.kimjaemin.springbootblog.config.error.exception.ArticleNotFoundException;
import me.kimjaemin.springbootblog.domain.Article;
import me.kimjaemin.springbootblog.dto.ArticleListViewResponse;
import me.kimjaemin.springbootblog.dto.ArticleViewResponse;
import me.kimjaemin.springbootblog.service.BlogService;
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

    @GetMapping("/articles")
    public String getArticles(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {
        Page<ArticleListViewResponse> page = blogService.getPage(pageable)
                .map(ArticleListViewResponse::new);
        model.addAttribute("page", page);

        return "articleList";
    }


    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model) {
         try {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        } catch (ArticleNotFoundException e) {
            model.addAttribute("error", e.getErrorCode().getCode() + ": " + e.getMessage());
        }
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
                model.addAttribute("error", e.getErrorCode().getCode() + ": " + e.getMessage());
            }
        }
        return "writeArticle";
    }

}
