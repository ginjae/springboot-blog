package me.kimjaemin.springbootblog.config.error.exception;

import me.kimjaemin.springbootblog.config.error.ErrorCode;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }

}
