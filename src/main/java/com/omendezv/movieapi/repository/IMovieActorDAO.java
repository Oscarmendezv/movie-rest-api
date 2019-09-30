package com.omendezv.movieapi.repository;

import com.omendezv.movieapi.repository.Entity.MovieActor;
import com.omendezv.movieapi.repository.Entity.MovieActorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMovieActorDAO extends JpaRepository<MovieActor, MovieActorId> {

    List<MovieActor> findByMovieActorIdMovieId(Long movieId);

    List<MovieActor> findByMovieActorIdActorId(Long actorId);

    @Query("SELECT ma FROM MovieActor ma WHERE ma.movieActorId.movieId LIKE :movieId AND ma.movieActorId.actorId LIKE :actorId")
    Optional<MovieActor> findByCompositeId(@Param("actorId") Long actorId, @Param("movieId") Long movieId);
}
