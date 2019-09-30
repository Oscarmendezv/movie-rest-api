package com.omendezv.movieapi.service;

import com.omendezv.movieapi.domain.DTO.ActorDTO;
import com.omendezv.movieapi.domain.DTO.MovieDTO;
import com.omendezv.movieapi.domain.Entity.Actor;
import com.omendezv.movieapi.domain.Entity.Movie;
import com.omendezv.movieapi.domain.Entity.MovieActor;
import com.omendezv.movieapi.domain.Entity.MovieActorId;
import com.omendezv.movieapi.domain.enums.ActorRolesEnum;
import com.omendezv.movieapi.domain.enums.MovieGenresEnum;
import com.omendezv.movieapi.exceptions.MovieInsertException;
import com.omendezv.movieapi.exceptions.MovieNotFoundException;
import com.omendezv.movieapi.repository.IActorDAO;
import com.omendezv.movieapi.repository.IMovieActorDAO;
import com.omendezv.movieapi.repository.IMovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private IMovieDAO movieDAO;

    @Autowired
    private IMovieActorDAO movieActorDAO;

    @Autowired
    private IActorDAO actorDAO;

    @Autowired
    private IActorService actorService;

    @Transactional
    public List<MovieDTO> findAllMovies() {
        return  processMovieListToMovieDTO(movieDAO.findAll());
    }

    @Transactional
    public MovieDTO findMovieById(Long id) {
        return movieEntityToDTO(movieDAO.findById(id).orElseThrow(() -> new MovieNotFoundException()), true);
    }

    @Transactional
    public List<MovieDTO> findMovieByGenre(MovieGenresEnum genre) {
        return processMovieListToMovieDTO(movieDAO.findByMovieGenre(genre));
    }

    @Transactional
    public MovieDTO createMovie(MovieDTO movieDTO) {
        if (movieDTO.getId() != null && movieDAO.findById(movieDTO.getId()).isPresent())
            throw new MovieInsertException();

        Movie movie = movieDTOtoEntity(movieDTO);
        Set<MovieActor> movieActorSet = movie.getActorsWhoPerformed();
        movie.setActorsWhoPerformed(Collections.emptySet());
        movie = movieDAO.save(movie);

        Set<MovieActor> newMovieActorSet = fillMovieActorSet(movieActorSet, movie);
        movie.setActorsWhoPerformed(newMovieActorSet);

        return movieEntityToDTO(movie, true);
    }

    @Transactional
    public List<MovieDTO> findMoviesByTitle(String title) {
        return processMovieListToMovieDTO(movieDAO.findMoviesByTitle(title));
    }

    @Transactional
    public MovieDTO updateMovie(MovieDTO updatedMovie) {
        MovieDTO currentMovie = findMovieById(updatedMovie.getId());
        if (currentMovie.equals(updatedMovie))
            return currentMovie;

        return movieEntityToDTO(movieDAO.save(movieDTOtoEntity(updatedMovie)), true);
    }

    @Transactional
    public void deleteMovieById(Long id) {
        findMovieById(id);
        movieDAO.deleteById(id);
    }

    public MovieDTO movieEntityToDTO(Movie movie, boolean includeActors) {
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setYear(movie.getYear());
        if (movie.getMovieGenre() != null) movieDTO.setMovieGenre(movie.getMovieGenre().toString());
        if (movie.getMovieSubgenre() != null) movieDTO.setMovieSubgenre(movie.getMovieSubgenre().toString());
        if (includeActors) movieDTO.setActorsWhoPerformed(setActorsToMovieDTO(movieDTO));

        return movieDTO;
    }

    public Movie movieDTOtoEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();

        if (movieDTO.getId() != null) movie.setId(movieDTO.getId());
        movie.setYear(movieDTO.getYear());
        movie.setTitle(movieDTO.getTitle());
        if (movieDTO.getMovieGenre() != null && movieDTO.getMovieGenre() != "") movie.setMovieGenre(MovieGenresEnum.valueOf(movieDTO.getMovieGenre()));
        if (movieDTO.getMovieSubgenre() != null && movieDTO.getMovieSubgenre() != "") movie.setMovieSubgenre(MovieGenresEnum.valueOf(movieDTO.getMovieSubgenre()));

        Set<MovieActor> movieActors = new HashSet<>();
        if (!CollectionUtils.isEmpty(movieDTO.getActorsWhoPerformed())) {
            movieActors = fillMovieEntityActors(movieDTO.getActorsWhoPerformed(), movie);
        }

        movie.setActorsWhoPerformed(movieActors);
        return movie;
    }

    public Set<MovieActor> fillMovieEntityActors(List<ActorDTO> actorsWhoPerformed, Movie movie) {
        Set<MovieActor> movieActors = new HashSet<>();
        for (ActorDTO actorDTO :actorsWhoPerformed) {
            MovieActor movieActor;
            if (movie.getId() != null)
                movieActor = movieActorDAO.findByCompositeId(actorDTO.getId(), movie.getId()).orElse(new MovieActor());
            else
                movieActor = new MovieActor();

            Actor actor = actorDAO.findById(actorDTO.getId()).get();
            Actor newActor = actorService.actorDTOtoEntity(actorDTO);
            actor.setName(newActor.getName());
            actor.setDateOfDeath(newActor.getDateOfDeath());
            actor.setDateOfBirth(newActor.getDateOfBirth());

            movieActor.setRole(ActorRolesEnum.valueOf(actorDTO.getRoleInMovie()));
            movieActor.setActor(actor);
            movieActors.add(movieActor);
        }

        return movieActors;
    }

    public List<ActorDTO> setActorsToMovieDTO(MovieDTO movieDTO) {
        List<ActorDTO> actorsWhoPerformedInMovie = new ArrayList<>();
        List<MovieActor> movieActorEntity = movieActorDAO.findByMovieActorIdMovieId(movieDTO.getId());

        if (!CollectionUtils.isEmpty(movieActorEntity)) {
            movieActorEntity.forEach(movieActor -> {
                ActorDTO actorDTO = actorService.actorEntityToDTO(actorDAO.findById(movieActor.getMovieActorId().getActorId()).get(), false);
                actorDTO.setRoleInMovie(movieActor.getRole().toString());
                actorsWhoPerformedInMovie.add(actorDTO);
            });
        }

        return actorsWhoPerformedInMovie;
    }

    public List<MovieDTO> processMovieListToMovieDTO(List<Movie> movieList) {
        return movieList.stream().map(movie -> movieEntityToDTO(movie, true)).collect(Collectors.toList());
    }

    public Set<MovieActor> fillMovieActorSet(Set<MovieActor> movieActorSet, Movie movie) {
        Set<MovieActor> newMovieActorSet = new HashSet<>();
        for (MovieActor movieActor: movieActorSet) {
            movieActor.setMovieActorId(new MovieActorId(movieActor.getActor().getId(), movie.getId()));
            movieActor.setMovie(movie);
            newMovieActorSet.add(movieActorDAO.save(movieActor));
        }

        return newMovieActorSet;
    }
}
