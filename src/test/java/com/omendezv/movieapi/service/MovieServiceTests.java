package com.omendezv.movieapi.service;

import com.omendezv.movieapi.DTO.MovieDTO;
import com.omendezv.movieapi.mapper.MovieMapper;
import com.omendezv.movieapi.repository.Entity.Movie;
import com.omendezv.movieapi.repository.Entity.enums.MovieGenresEnum;
import com.omendezv.movieapi.exceptions.MovieInsertException;
import com.omendezv.movieapi.exceptions.MovieNotFoundException;
import com.omendezv.movieapi.repository.IMovieActorDAO;
import com.omendezv.movieapi.repository.IMovieDAO;
import com.omendezv.movieapi.service.Imp.MovieServiceImp;
import org.junit.Assert;
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

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieServiceImp movieService;

    private List<Movie> movies;
    private Movie savedMovie1;
    private Movie savedMovie2;
    private Movie savedMovie3;
    private MovieDTO savedMovieDTO;

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

        savedMovieDTO = new MovieDTO();
        savedMovieDTO.setId(2L);
        savedMovieDTO.setTitle("American History X");
        savedMovieDTO.setMovieGenre("DRAMA");
        savedMovieDTO.setYear(2002);
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
        movieService.findMovieById(0L);
    }

    @Test
    public void createMovieTest() {
        Mockito.when(movieDAO.save(any(Movie.class))).thenReturn(savedMovie2);
        Long createdMovieId = movieService.createMovie(movieMapper.movieEntityToDTO(savedMovie2, false));

        assertNotNull(createdMovieId);
        assertEquals(savedMovie2.getId(), createdMovieId);
    }

    @Test(expected = MovieInsertException.class)
    public void createMovieExceptionTest() {
        Mockito.when(movieDAO.findById(savedMovie3.getId())).thenReturn(Optional.of(savedMovie3));
        movieService.createMovie(movieMapper.movieEntityToDTO(savedMovie3, false));
    }

    @Test
    public void findMovieByGenreTest() {
        Mockito.when(movieDAO.findByMovieGenre(MovieGenresEnum.SCI_FI)).thenReturn(movies);
        Mockito.when(movieActorDAO.findByMovieActorIdMovieId(savedMovie2.getId())).thenReturn(Collections.emptyList());
        List<MovieDTO> moviesByGenre = movieService.findMovieByGenre(MovieGenresEnum.SCI_FI);

        assertNotNull(moviesByGenre);
        assertEquals(movies.size(), moviesByGenre.size());
    }

    @Test
    public void updateMovieTest() {
        Mockito.when(movieDAO.findById(savedMovie2.getId())).thenReturn(Optional.of(savedMovie3));
        Mockito.when(movieActorDAO.findByMovieActorIdMovieId(savedMovie2.getId())).thenReturn(null);
        Mockito.when(movieDAO.save(any(Movie.class))).thenReturn(savedMovie2);
        Mockito.when(movieMapper.movieEntityToDTO(savedMovie2, true)).thenReturn(savedMovieDTO);
        Mockito.when(movieMapper.movieEntityToDTO(savedMovie3, true)).thenReturn(savedMovieDTO);
        Long id = movieService.updateMovie(savedMovieDTO);

        assertNotNull(id);
        assertEquals(savedMovie2.getId(), id);
    }

    @Test(expected = MovieNotFoundException.class)
    public void updateMovieNotFoundTest() {
        Mockito.when(movieDAO.findById(savedMovie2.getId())).thenThrow(MovieNotFoundException.class);
        MovieDTO savedMovieDTO = movieMapper.movieEntityToDTO(savedMovie2, false);
        movieService.updateMovie(savedMovieDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterYear() {
        Mockito.when(movieDAO.save(any(Movie.class))).thenReturn(savedMovie1);
        savedMovie1.setYear(20000);
        movieService.createMovie(movieMapper.movieEntityToDTO(savedMovie2, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLowerYear() {
        Mockito.when(movieDAO.save(any(Movie.class))).thenReturn(savedMovie1);
        savedMovie1.setYear(1880);
        movieService.createMovie(movieMapper.movieEntityToDTO(savedMovie2, false));
    }
}
