package com.omendezv.movieapi.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class DateUtils {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public SimpleDateFormat getFormatter() {
        return formatter;
    }
}
