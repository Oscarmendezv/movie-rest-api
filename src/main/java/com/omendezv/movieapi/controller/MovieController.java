package com.omendezv.movieapi.controller;

import com.omendezv.movieapi.domain.DTO.MovieDTO;
import com.omendezv.movieapi.domain.Entity.Movie;
import com.omendezv.movieapi.domain.enums.MovieGenresEnum;
import com.omendezv.movieapi.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private IMovieService movieService;

    /**
     * Returns a list of every Movie present in the Database.
     * @return List of movies
     */
    @GetMapping
    public List<MovieDTO> getAllMovies() {
        return movieService.findAllMovies();
    }

    /**
     * Returns (if present) a movie with a certain id value.
     * @param id - Long type
     * @return - Movie details (if found)
     */
    @GetMapping("/{id}")
    public MovieDTO getMovieById(@PathVariable Long id) {
        return movieService.findMovieById(id);
    }

    /**
     * Returns a list of movies which title contains/is similar to the one provided.
     * @param title - String with the title or word used to make the search.
     * @return - List of movies found.
     */
    @GetMapping("/search/{title}")
    public List<MovieDTO> getMoviesByTitle(@PathVariable String title) {
        return movieService.findMoviesByTitle(title);
    }

    /**
     * Returns a list of movies whose genre/subgenre is the one provided.
     * @param genre - Genre you want to search by (should match a genre in the MovieGenresEnum.class).
     * @return - List of movies found.
     */
    @GetMapping("/genre/{genre}")
    public List<MovieDTO> getMoviesByGenre(@PathVariable String genre) {
        return movieService.findMovieByGenre(MovieGenresEnum.valueOf(genre));
    }

    /**
     *
     * @param movieDTO
     * @return
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public MovieDTO createMovie(@RequestBody MovieDTO movieDTO) {
        return movieService.createMovie(movieDTO);
    }

    /**
     *
     * @param id
     * @param movie
     * @return
     */
    @PutMapping("/{id}")
    public MovieDTO updateMovie(@PathVariable Long id, @RequestBody MovieDTO movie) {
        return movieService.updateMovie(movie);
    }

    /**
     * Deletes the movie with the specified ID.
     * @param id - Long type.
     */
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovieById(id);
    }

}
