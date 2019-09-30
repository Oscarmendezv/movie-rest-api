package com.omendezv.movieapi.exceptions;

public abstract class MovieApiException extends RuntimeException {

    private String code;

    public MovieApiException() {
        super();
    }

    public abstract String getCode();
}
