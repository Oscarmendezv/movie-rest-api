package com.omendezv.movieapi.service.Imp;

import com.omendezv.movieapi.DTO.ActorDTO;
import com.omendezv.movieapi.DTO.MovieDTO;
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
    private ActorService actorService;

    @Autowired
    public MovieServiceImp(IMovieDAO movieDAO, IMovieActorDAO movieActorDAO, IActorDAO actorDAO, @Lazy ActorService actorService) {
        this.movieDAO = movieDAO;
        this.movieActorDAO = movieActorDAO;
        this.actorDAO = actorDAO;
        this.actorService = actorService;
    }

    public List<MovieDTO> findAllMovies() {
        return  processMovieListToMovieDTO(movieDAO.findAll());
    }

    public MovieDTO findMovieById(Long id) {
        return movieEntityToDTO(movieDAO.findById(id).orElseThrow(() -> new MovieNotFoundException()), true);
    }

    public List<MovieDTO> findMovieByGenre(MovieGenresEnum genre) {
        return processMovieListToMovieDTO(movieDAO.findByMovieGenre(genre));
    }

    public List<MovieDTO> findMoviesByTitle(String title) {
        return processMovieListToMovieDTO(movieDAO.findMoviesByTitle(title));
    }

    @Transactional
    public Long createMovie(MovieDTO movieDTO) {
        if (movieDTO.getId() != null && movieDAO.findById(movieDTO.getId()).isPresent())
            throw new MovieInsertException();

        Movie movie = movieDTOtoEntity(movieDTO);
        Set<MovieActor> movieActorSet = movie.getActorsWhoPerformed();
        movie.setActorsWhoPerformed(Collections.emptySet());
        movie = movieDAO.save(movie);

        Set<MovieActor> newMovieActorSet = fillMovieActorSet(movieActorSet, movie);
        movie.setActorsWhoPerformed(newMovieActorSet);

        return movie.getId();
    }

    @Transactional
    public Long updateMovie(MovieDTO updatedMovie) {
        MovieDTO currentMovie = findMovieById(updatedMovie.getId());
        if (currentMovie.equals(updatedMovie))
            return currentMovie.getId();

        return movieDAO.save(movieDTOtoEntity(updatedMovie)).getId();
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
        if (movieDTO.getYear() > ZonedDateTime.now(ZoneId.of("Europe/Madrid")).getYear() || movieDTO.getYear() < 1888) {
            throw new IllegalArgumentException("Year must be valid");
        }
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
