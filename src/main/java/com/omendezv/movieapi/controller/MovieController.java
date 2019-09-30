package com.omendezv.movieapi.controller;

import com.omendezv.movieapi.DTO.MovieDTO;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;

public interface MovieController {

    /**
     * Returns a list of every Movie present in the Database.
     * @return List of movies
     */
    List<MovieDTO> getAllMovies();

    /**
     * Returns (if present) a movie with a certain id value.
     * @param id - Long type
     * @return - Movie details (if found)
     */
    MovieDTO getMovieById(Long id);

    /**
     * Returns a list of movies which title contains/is similar to the one provided.
     * @param title - String with the title or word used to make the search.
     * @return - List of movies found.
     */
    List<MovieDTO> getMoviesByTitle(String title);

    /**
     * Returns a list of movies whose genre/subgenre is the one provided.
     * @param genre - Genre you want to search by (should match a genre in the MovieGenresEnum.class).
     * @return - List of movies found.
     */
    List<MovieDTO> getMoviesByGenre(String genre);

    /**
     * Creates a new Movie with the specified details.
     * @param movieDTO
     * @return
     */
    ResponseEntity<?> createMovie(MovieDTO movieDTO) throws URISyntaxException;

    /**
     * Updates an already existing movie.
     * @param id - Id of the movie to update
     * @param movie
     * @return
     */
    ResponseEntity<?> updateMovie(Long id, MovieDTO movie);

    /**
     * Deletes the movie with the specified ID.
     * @param id - Long type.
     */
    void deleteMovie(Long id);
}
