package com.omendezv.movieapi.DTO;

import java.io.Serializable;
import java.util.List;

public class MovieDTO implements Serializable {

    private Long id;
    private String title;
    private int year;
    private String movieGenre;
    private String movieSubgenre;
    private List<Long> actorsWhoPerformed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getMovieSubgenre() {
        return movieSubgenre;
    }

    public void setMovieSubgenre(String movieSubgenre) {
        this.movieSubgenre = movieSubgenre;
    }

    public List<Long> getActorsWhoPerformed() {
        return actorsWhoPerformed;
    }

    public void setActorsWhoPerformed(List<Long> actorsWhoPerformed) {
        this.actorsWhoPerformed = actorsWhoPerformed;
    }
}
