package com.omendezv.movieapi.DTO;

import java.io.Serializable;
import java.util.List;

public class ActorDTO implements Serializable {

    private Long id;
    private String name;
    private String dateOfBirth;
    private String dateOfDeath;
    private List<Long> movieAppearances;
    private String roleInMovie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public List<Long> getMovieAppearances() {
        return movieAppearances;
    }

    public void setMovieAppearances(List<Long> movieAppearances) {
        this.movieAppearances = movieAppearances;
    }

    public String getRoleInMovie() {
        return roleInMovie;
    }

    public void setRoleInMovie(String roleInMovie) {
        this.roleInMovie = roleInMovie;
    }
}
