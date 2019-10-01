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

    void deleteMovieById(Long id);
}