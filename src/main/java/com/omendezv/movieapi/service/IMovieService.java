package com.omendezv.movieapi.service;

import com.omendezv.movieapi.domain.DTO.ActorDTO;
import com.omendezv.movieapi.domain.DTO.MovieDTO;
import com.omendezv.movieapi.domain.Entity.Movie;
import com.omendezv.movieapi.domain.Entity.MovieActor;
import com.omendezv.movieapi.domain.enums.MovieGenresEnum;

import java.util.List;
import java.util.Set;

public interface IMovieService {

    List<MovieDTO> findAllMovies();

    List<MovieDTO> findMovieByGenre(MovieGenresEnum genre);

    List<MovieDTO> findMoviesByTitle(String title);

    MovieDTO findMovieById(Long id);

    MovieDTO createMovie(MovieDTO movieDTO);

    MovieDTO updateMovie(MovieDTO movieDTO);

    MovieDTO movieEntityToDTO(Movie movie, boolean includeActors);

    List<ActorDTO> setActorsToMovieDTO(MovieDTO movieDTO);

    List<MovieDTO> processMovieListToMovieDTO(List<Movie> movieList);

    Movie movieDTOtoEntity(MovieDTO movieDTO);

    void deleteMovieById(Long id);

    Set<MovieActor> fillMovieActorSet(Set<MovieActor> movieActorSet, Movie movie);

    Set<MovieActor> fillMovieEntityActors(List<ActorDTO> actorsWhoPerformed, Movie movie);
}