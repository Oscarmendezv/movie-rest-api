package com.omendezv.movieapi.service;

import com.omendezv.movieapi.DTO.ActorDTO;
import com.omendezv.movieapi.DTO.MovieDTO;
import com.omendezv.movieapi.repository.Entity.Movie;
import com.omendezv.movieapi.repository.Entity.MovieActor;
import com.omendezv.movieapi.repository.Entity.enums.MovieGenresEnum;

import java.util.List;
import java.util.Set;

public interface MovieService {

    List<MovieDTO> findAllMovies();

    List<MovieDTO> findMovieByGenre(MovieGenresEnum genre);

    List<MovieDTO> findMoviesByTitle(String title);

    MovieDTO findMovieById(Long id);

    Long createMovie(MovieDTO movieDTO);

    Long updateMovie(MovieDTO movieDTO);

    MovieDTO movieEntityToDTO(Movie movie, boolean includeActors);

    List<ActorDTO> setActorsToMovieDTO(MovieDTO movieDTO);

    List<MovieDTO> processMovieListToMovieDTO(List<Movie> movieList);

    Movie movieDTOtoEntity(MovieDTO movieDTO);

    void deleteMovieById(Long id);

    Set<MovieActor> fillMovieActorSet(Set<MovieActor> movieActorSet, Movie movie);

    Set<MovieActor> fillMovieEntityActors(List<ActorDTO> actorsWhoPerformed, Movie movie);
}