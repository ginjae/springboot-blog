package me.kimjaemin.springbootblog.config.error.exception;

import me.kimjaemin.springbootblog.config.error.ErrorCode;

public class UnauthorizedException extends BusinessBaseException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public UnauthorizedException() {
        super(ErrorCode.FORBIDDEN);
    }

}
