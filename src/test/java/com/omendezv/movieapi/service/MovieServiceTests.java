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

    private List<MovieDTO> moviesDTO;
    private MovieDTO savedMovieDTO1;
    private MovieDTO savedMovieDTO2;
    private MovieDTO savedMovieDTO3;

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

        moviesDTO = new ArrayList<>();
        savedMovieDTO1 = new MovieDTO();
        savedMovieDTO2 = new MovieDTO();
        savedMovieDTO3 = new MovieDTO();


        savedMovieDTO1.setId(1L);
        savedMovieDTO1.setTitle("Buscando a Nemo");
        savedMovieDTO1.setMovieGenre(MovieGenresEnum.ADVENTURE.toString());
        savedMovieDTO1.setYear(2001);

        savedMovieDTO2.setId(2L);
        savedMovieDTO2.setTitle("American History X");
        savedMovieDTO2.setMovieGenre(MovieGenresEnum.DRAMA.toString());
        savedMovieDTO2.setYear(2002);

        savedMovieDTO3.setId(3L);
        savedMovieDTO3.setTitle("Yo robot");
        savedMovieDTO3.setMovieGenre(MovieGenresEnum.SCI_FI.toString());
        savedMovieDTO3.setYear(2003);

        moviesDTO.add(savedMovieDTO1);
        moviesDTO.add(savedMovieDTO2);
        moviesDTO.add(savedMovieDTO3);
    }

    @Test
    public void findAllTest() {
        Mockito.when(movieDAO.findAll()).thenReturn(movies);
        Mockito.when(movieMapper.processMovieListToMovieDTO(movies)).thenReturn(moviesDTO);
        List<MovieDTO> obtainedMovies = movieService.findAllMovies();

        assertNotNull(obtainedMovies);
    }

    @Test
    public void findMovieByIdTest() {
        Mockito.when(movieDAO.findById(1L)).thenReturn(Optional.of(savedMovie1));
        Mockito.when(movieMapper.movieEntityToDTO(savedMovie1, true)).thenReturn(savedMovieDTO1);
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
        Mockito.when(movieMapper.movieEntityToDTO(savedMovie2, true)).thenReturn(savedMovieDTO2);
        Mockito.when(movieMapper.movieDTOtoEntity(savedMovieDTO2)).thenReturn(savedMovie2);
        Long createdMovieId = movieService.createMovie(savedMovieDTO2);

        assertNotNull(createdMovieId);
        assertEquals(savedMovie2.getId(), createdMovieId);
    }

    @Test(expected = MovieInsertException.class)
    public void createMovieExceptionTest() {
        Mockito.when(movieDAO.findById(savedMovie3.getId())).thenReturn(Optional.of(savedMovie3));
        movieService.createMovie(savedMovieDTO3);
    }

    @Test
    public void findMovieByGenreTest() {
        Mockito.when(movieDAO.findByMovieGenre(MovieGenresEnum.SCI_FI)).thenReturn(movies);
        Mockito.when(movieActorDAO.findByMovieActorIdMovieId(savedMovie2.getId())).thenReturn(Collections.emptyList());
        Mockito.when(movieMapper.processMovieListToMovieDTO(movies)).thenReturn(moviesDTO);
        List<MovieDTO> moviesByGenre = movieService.findMovieByGenre(MovieGenresEnum.SCI_FI);

        assertNotNull(moviesByGenre);
        assertEquals(movies.size(), moviesByGenre.size());
    }

    @Test
    public void updateMovieTest() {
        Mockito.when(movieDAO.findById(savedMovie2.getId())).thenReturn(Optional.of(savedMovie3));
        Mockito.when(movieActorDAO.findByMovieActorIdMovieId(savedMovie2.getId())).thenReturn(null);
        Mockito.when(movieDAO.save(any(Movie.class))).thenReturn(savedMovie2);
        Mockito.when(movieMapper.movieDTOtoEntity(savedMovieDTO2)).thenReturn(savedMovie2);
        Mockito.when(movieMapper.movieEntityToDTO(savedMovie3, true)).thenReturn(savedMovieDTO3);
        Long id = movieService.updateMovie(savedMovieDTO2);

        assertNotNull(id);
        assertEquals(savedMovie2.getId(), id);
    }

    @Test(expected = MovieNotFoundException.class)
    public void updateMovieNotFoundTest() {
        Mockito.when(movieDAO.findById(savedMovie2.getId())).thenThrow(MovieNotFoundException.class);
        movieService.updateMovie(savedMovieDTO2);
    }
}
