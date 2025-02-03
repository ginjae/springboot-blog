package me.kimjaemin.springbootblog.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E4", "존재하지 않는 엔티티입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "E5", "권한이 없습니다."),

    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "A1", "존재하지 않는 글입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C1", "존재하지 않는 댓글입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CA1", "존재하지 않는 카테고리입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
