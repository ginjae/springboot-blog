package me.kimjaemin.springbootblog.config.error.exception;

import me.kimjaemin.springbootblog.config.error.ErrorCode;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }

}
