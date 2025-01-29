package me.kimjaemin.springbootblog.config.error.exception;

import me.kimjaemin.springbootblog.config.error.ErrorCode;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }

}
