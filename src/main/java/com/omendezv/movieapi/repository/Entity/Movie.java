package com.omendezv.movieapi.repository.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.omendezv.movieapi.repository.Entity.enums.MovieGenresEnum;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "movies")
@NaturalIdCache
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Movie implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long movieId;

    @Column(name = "title")
    @NaturalId
    private String title;

    @Column(length = 4)
    private int year;

    @Enumerated(EnumType.STRING)
    private MovieGenresEnum movieGenre;

    @Enumerated(EnumType.STRING)
    private MovieGenresEnum movieSubgenre;

    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<MovieActor> actorsWhoPerformed;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_views",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "movie_id") }
    )
    private Set<User> usersThatWatchedIt;

    public Movie() {}

    public Movie(String title, int year, Set<MovieActor> actorsWhoPerformed) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.actorsWhoPerformed = actorsWhoPerformed;
    }

    @Override
    public Long getId() {
        return movieId;
    }

    public void setId(Long id) {
        this.movieId = id;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
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

    public Set<MovieActor> getActorsWhoPerformed() {
        return actorsWhoPerformed;
    }

    public void setActorsWhoPerformed(Set<MovieActor> actorsWhoPerformed) {
        this.actorsWhoPerformed = actorsWhoPerformed;
    }

    public MovieGenresEnum getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(MovieGenresEnum movieGenre) {
        this.movieGenre = movieGenre;
    }

    public MovieGenresEnum getMovieSubgenre() {
        return movieSubgenre;
    }

    public void setMovieSubgenre(MovieGenresEnum movieSubgenre) {
        this.movieSubgenre = movieSubgenre;
    }

    public Set<User> getUsersThatWatchedIt() {
        return usersThatWatchedIt;
    }

    public void setUsersThatWatchedIt(Set<User> usersThatWatchedIt) {
        this.usersThatWatchedIt = usersThatWatchedIt;
    }
}
