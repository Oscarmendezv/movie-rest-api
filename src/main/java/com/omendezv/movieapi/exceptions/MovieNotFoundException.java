package com.omendezv.movieapi.exceptions;

public class MovieNotFoundException extends MovieApiException {

    private static final String code = "001";

    public String getCode() {
        return code;
    }
}
