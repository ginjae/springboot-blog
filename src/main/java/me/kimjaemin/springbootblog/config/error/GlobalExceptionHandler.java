package me.kimjaemin.springbootblog.config.error;

import jdk.jfr.Experimental;
import lombok.extern.slf4j.Slf4j;
import me.kimjaemin.springbootblog.config.error.exception.BusinessBaseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        List<String> errorList = new java.util.ArrayList<>(e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList());
        List<String> priorityOrder = Arrays.asList(
                "제목을 입력해 주세요.", "제목은 1-30자로 입력해 주세요.", "내용을 입력해 주세요.",
                "이메일을 입력해 주세요.", "올바른 이메일 주소를 입력해 주세요.", "비밀번호를 입력해 주세요.", "비밀번호는 8-64자로 입력해 주세요.",
                "비밀번호 확인을 입력해 주세요.", "비밀번호 확인은 8-64자로 입력해 주세요.", "닉네임을 입력해 주세요.", "닉네임은 2-15자로 입력해 주세요.");
        errorList.sort(Comparator.comparingInt(priorityOrder::indexOf));
        return createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE, errorList.get(0));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handle(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        return createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
    }

    @ExceptionHandler(BusinessBaseException.class)
    protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e) {
        log.error("BusinessBaseException", e);
        return createErrorResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception e) {
        e.printStackTrace();
        log.error("Exception", e);
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode),
                errorCode.getHttpStatus()
        );
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode, String message) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode, message),
                errorCode.getHttpStatus()
        );
    }

}
