package com.omendezv.movieapi.service.Imp;

import com.omendezv.movieapi.DTO.ActorDTO;
import com.omendezv.movieapi.DTO.MovieDTO;
import com.omendezv.movieapi.mapper.ActorMapper;
import com.omendezv.movieapi.mapper.MovieMapper;
import com.omendezv.movieapi.repository.Entity.Actor;
import com.omendezv.movieapi.repository.Entity.Movie;
import com.omendezv.movieapi.repository.Entity.MovieActor;
import com.omendezv.movieapi.repository.Entity.MovieActorId;
import com.omendezv.movieapi.repository.Entity.enums.ActorRolesEnum;
import com.omendezv.movieapi.repository.Entity.enums.MovieGenresEnum;
import com.omendezv.movieapi.exceptions.MovieInsertException;
import com.omendezv.movieapi.exceptions.MovieNotFoundException;
import com.omendezv.movieapi.repository.IActorDAO;
import com.omendezv.movieapi.repository.IMovieActorDAO;
import com.omendezv.movieapi.repository.IMovieDAO;
import com.omendezv.movieapi.service.ActorService;
import com.omendezv.movieapi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImp implements MovieService {

    private IMovieDAO movieDAO;
    private IMovieActorDAO movieActorDAO;
    private IActorDAO actorDAO;
    private MovieMapper movieMapper;
    private ActorMapper actorMapper;

    @Autowired
    public MovieServiceImp(IMovieDAO movieDAO, IMovieActorDAO movieActorDAO, IActorDAO actorDAO, MovieMapper movieMapper, ActorMapper actorMapper) {
        this.movieDAO = movieDAO;
        this.movieActorDAO = movieActorDAO;
        this.actorDAO = actorDAO;
        this.movieMapper = movieMapper;
        this.actorMapper = actorMapper;
    }

    public List<MovieDTO> findAllMovies() {
        return  movieMapper.processMovieListToMovieDTO(movieDAO.findAll());
    }

    public MovieDTO findMovieById(Long id) {
        return movieMapper.movieEntityToDTO(movieDAO.findById(id).orElseThrow(() -> new MovieNotFoundException()), true);
    }

    public List<MovieDTO> findMovieByGenre(MovieGenresEnum genre) {
        return movieMapper.processMovieListToMovieDTO(movieDAO.findByMovieGenre(genre));
    }

    public List<MovieDTO> findMoviesByTitle(String title) {
        return movieMapper.processMovieListToMovieDTO(movieDAO.findMoviesByTitle(title));
    }

    @Transactional
    public Long createMovie(MovieDTO movieDTO) {
        if (movieDTO.getId() != null && movieDAO.findById(movieDTO.getId()).isPresent())
            throw new MovieInsertException();

        Movie movie = movieMapper.movieDTOtoEntity(movieDTO);
        Set<MovieActor> movieActorSet = movie.getActorsWhoPerformed();
        movie.setActorsWhoPerformed(Collections.emptySet());
        movie = movieDAO.save(movie);

        Set<MovieActor> newMovieActorSet = movieMapper.fillMovieActorSet(movieActorSet, movie);
        movie.setActorsWhoPerformed(newMovieActorSet);

        return movie.getId();
    }

    @Transactional
    public Long updateMovie(MovieDTO updatedMovie) {
        MovieDTO currentMovie = findMovieById(updatedMovie.getId());
        if (currentMovie.equals(updatedMovie))
            return currentMovie.getId();

        return movieDAO.save(movieMapper.movieDTOtoEntity(updatedMovie)).getId();
    }

    @Transactional
    public void deleteMovieById(Long id) {
        findMovieById(id);
        movieDAO.deleteById(id);
    }
}
