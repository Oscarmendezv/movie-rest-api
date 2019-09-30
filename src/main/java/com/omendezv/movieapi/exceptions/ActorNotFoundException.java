package com.omendezv.movieapi.exceptions;

public class ActorNotFoundException extends MovieApiException {

    private static final String code = "003";

    public String getCode() {
        return code;
    }

}
