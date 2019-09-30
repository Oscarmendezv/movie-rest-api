package com.omendezv.movieapi.exceptions;

public class MovieInsertException extends MovieApiException {

    private static final String code = "002";

    public String getCode() {
        return code;
    }
}
