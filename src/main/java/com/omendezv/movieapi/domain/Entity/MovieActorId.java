package com.omendezv.movieapi.domain.Entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MovieActorId implements Serializable {
    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "movie_id")
    private Long movieId;

    public MovieActorId() {}

    public MovieActorId(Long actorId, Long movieId) {
        this.actorId = actorId;
        this.movieId = movieId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, movieId);
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
