package com.omendezv.movieapi.controller.Imp;

import com.omendezv.movieapi.controller.MovieController;
import com.omendezv.movieapi.DTO.MovieDTO;
import com.omendezv.movieapi.repository.Entity.enums.MovieGenresEnum;
import com.omendezv.movieapi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieControllerImp implements MovieController {

    private MovieService movieService;

    @Autowired
    public MovieControllerImp(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDTO> getAllMovies() {
        return movieService.findAllMovies();
    }

    @GetMapping("/{id}")
    public MovieDTO getMovieById(@PathVariable Long id) {
        return movieService.findMovieById(id);
    }

    @GetMapping("/search/{title}")
    public List<MovieDTO> getMoviesByTitle(@PathVariable String title) {
        return movieService.findMoviesByTitle(title);
    }

    @GetMapping("/genres/{genre}")
    public List<MovieDTO> getMoviesByGenre(@PathVariable String genre) {
        return movieService.findMovieByGenre(MovieGenresEnum.valueOf(genre));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createMovie(@RequestBody MovieDTO movieDTO) throws URISyntaxException {
        Long id = movieService.createMovie(movieDTO);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody MovieDTO movie) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(movieService.updateMovie(movie))
                .toUri()).build();
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovieById(id);
    }

}
