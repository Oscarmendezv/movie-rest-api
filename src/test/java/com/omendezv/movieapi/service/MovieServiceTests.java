package com.omendezv.movieapi.service;

import com.omendezv.movieapi.domain.DTO.MovieDTO;
import com.omendezv.movieapi.domain.Entity.Movie;
import com.omendezv.movieapi.domain.enums.MovieGenresEnum;
import com.omendezv.movieapi.exceptions.MovieInsertException;
import com.omendezv.movieapi.exceptions.MovieNotFoundException;
import com.omendezv.movieapi.repository.IMovieActorDAO;
import com.omendezv.movieapi.repository.IMovieDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MovieServiceTests {

    @Mock
    private IMovieDAO movieDAO;

    @Mock
    private IMovieActorDAO movieActorDAO;

    @InjectMocks
    private MovieService movieService;

    private List<Movie> movies;
    private Movie savedMovie1;
    private Movie savedMovie2;
    private Movie savedMovie3;

    @Before
    public void init() {
        movies = new ArrayList<>();

        savedMovie1 = new Movie();
        savedMovie2 = new Movie();
        savedMovie3 = new Movie();

        savedMovie1.setId(1L);
        savedMovie1.setTitle("Buscando a Nemo");
        savedMovie1.setMovieGenre(MovieGenresEnum.ADVENTURE);
        savedMovie1.setYear(2001);

        savedMovie2.setId(2L);
        savedMovie2.setTitle("American History X");
        savedMovie2.setMovieGenre(MovieGenresEnum.DRAMA);
        savedMovie2.setYear(2002);

        savedMovie3.setId(3L);
        savedMovie3.setTitle("Yo robot");
        savedMovie3.setMovieGenre(MovieGenresEnum.SCI_FI);
        savedMovie3.setYear(2003);

        movies.add(savedMovie1);
        movies.add(savedMovie2);
        movies.add(savedMovie3);
    }

    @Test
    public void findAllTest() {
        Mockito.when(movieDAO.findAll()).thenReturn(movies);
        List<MovieDTO> obtainedMovies = movieService.findAllMovies();

        assertNotNull(obtainedMovies);
    }

    @Test
    public void findMovieByIdTest() {
        Mockito.when(movieDAO.findById(1L)).thenReturn(Optional.of(savedMovie1));
        MovieDTO obtainedMovie = movieService.findMovieById(1L);

        assertNotNull(obtainedMovie);
    }

    @Test(expected = MovieNotFoundException.class)
    public void movieNotFoundByIdTest() {
        Mockito.when(movieDAO.findById(0L)).thenThrow(MovieNotFoundException.class);
        MovieDTO movie = movieService.findMovieById(0L);
    }

    @Test
    public void createMovieTest() {
        Mockito.when(movieDAO.save(any(Movie.class))).thenReturn(savedMovie2);
        MovieDTO createdMovie = movieService.createMovie(movieService.movieEntityToDTO(savedMovie2, false));

        assertNotNull(createdMovie);
    }

    @Test(expected = MovieInsertException.class)
    public void createMovieExceptionTest() {
        Mockito.when(movieDAO.findById(savedMovie3.getId())).thenReturn(Optional.of(savedMovie3));
        MovieDTO createdMovie = movieService.createMovie(movieService.movieEntityToDTO(savedMovie3, false));
    }

    @Test
    public void findMovieByGenreTest() {
        Mockito.when(movieDAO.findByMovieGenre(MovieGenresEnum.SCI_FI)).thenReturn(movies);
        Mockito.when(movieActorDAO.findByMovieActorIdMovieId(savedMovie2.getId())).thenReturn(Collections.emptyList());
        List<MovieDTO> moviesByGenre = movieService.findMovieByGenre(MovieGenresEnum.SCI_FI);

        assertNotNull(moviesByGenre);
    }

    @Test
    public void updateMovieTest() {
        MovieDTO savedMovieDTO = movieService.movieEntityToDTO(savedMovie2, true);
        Mockito.when(movieDAO.findById(savedMovie2.getId())).thenReturn(Optional.of(savedMovie3));
        Mockito.when(movieActorDAO.findByMovieActorIdMovieId(savedMovie2.getId())).thenReturn(null);
        Mockito.when(movieDAO.save(any(Movie.class))).thenReturn(savedMovie2);
        MovieDTO updatedMovie = movieService.updateMovie(savedMovieDTO);

        assertNotNull(updatedMovie);
    }

    @Test(expected = MovieNotFoundException.class)
    public void updateMovieNotFoundTest() {
        MovieDTO savedMovieDTO = movieService.movieEntityToDTO(savedMovie2, false);
        Mockito.when(movieDAO.findById(savedMovie2.getId())).thenThrow(MovieNotFoundException.class);
        movieService.updateMovie(savedMovieDTO);
    }
}
