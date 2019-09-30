package com.omendezv.movieapi.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomErrorHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(value = {MovieNotFoundException.class, ActorNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(MovieApiException ex, WebRequest request) {
        messageSourceAccessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, messageSourceAccessor.getMessage(ex.getCode()), ex));
    }

    @ExceptionHandler(value = {MovieInsertException.class, ActorInsertException.class})
    protected ResponseEntity<Object> handleInsertFailure(MovieApiException ex, WebRequest request) {
        messageSourceAccessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, messageSourceAccessor.getMessage(ex.getCode()), ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
