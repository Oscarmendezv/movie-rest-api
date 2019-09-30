package com.omendezv.movieapi.repository.Entity;

import com.omendezv.movieapi.repository.Entity.enums.ActorRolesEnum;

import javax.persistence.*;

@Entity(name = "MovieActor")
@Table(name = "movies_actors")
public class MovieActor {

    @EmbeddedId
    private MovieActorId movieActorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("actorId")
    private Actor actor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    private Movie movie;

    @Enumerated(EnumType.STRING)
    private ActorRolesEnum role;

    public MovieActorId getMovieActorId() {
        return movieActorId;
    }

    public void setMovieActorId(MovieActorId movieActorId) {
        this.movieActorId = movieActorId;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public ActorRolesEnum getRole() {
        return role;
    }

    public void setRole(ActorRolesEnum role) {
        this.role = role;
    }
}
