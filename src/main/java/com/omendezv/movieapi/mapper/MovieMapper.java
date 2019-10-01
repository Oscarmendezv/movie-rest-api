package com.omendezv.movieapi.mapper;

import com.omendezv.movieapi.DTO.MovieDTO;
import com.omendezv.movieapi.repository.Entity.Actor;
import com.omendezv.movieapi.repository.Entity.Movie;
import com.omendezv.movieapi.repository.Entity.MovieActor;
import com.omendezv.movieapi.repository.Entity.MovieActorId;
import com.omendezv.movieapi.repository.Entity.enums.MovieGenresEnum;
import com.omendezv.movieapi.repository.IActorDAO;
import com.omendezv.movieapi.repository.IMovieActorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MovieMapper {

    private IMovieActorDAO movieActorDAO;
    private IActorDAO actorDAO;

    public MovieMapper() {}

    @Autowired
    public MovieMapper(IMovieActorDAO movieActorDAO, IActorDAO actorDAO) {
        this.movieActorDAO = movieActorDAO;
        this.actorDAO = actorDAO;
    }

    public MovieDTO movieEntityToDTO(Movie movie, boolean includeActors) {
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setYear(movie.getYear());
        if (movie.getMovieGenre() != null) movieDTO.setMovieGenre(movie.getMovieGenre().toString());
        if (movie.getMovieSubgenre() != null) movieDTO.setMovieSubgenre(movie.getMovieSubgenre().toString());
        if(includeActors) movieDTO.setActorsWhoPerformed(setActorsToMovieDTO(movieDTO.getId()));

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

    public List<Long> setActorsToMovieDTO(Long movieId) {

        List<MovieActor> actorsWhoPerformedInMovie = movieActorDAO.findByMovieActorIdMovieId(movieId);
        List<Long> actorIdList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(actorsWhoPerformedInMovie)) {
            for (MovieActor movieActor : actorsWhoPerformedInMovie) {
                actorIdList.add(movieActor.getMovieActorId().getActorId());
            }
        }

        return actorIdList;
    }

    public Set<MovieActor> fillMovieEntityActors(List<Long> actorsWhoPerformed, Movie movie) {
        Set<MovieActor> movieActors = new HashSet<>();
        for (Long actorId :actorsWhoPerformed) {
            MovieActor movieActor;
            if (movie.getId() != null)
                movieActor = movieActorDAO.findByCompositeId(actorId, movie.getId()).orElse(new MovieActor());
            else
                movieActor = new MovieActor();

            Actor actor = actorDAO.findById(actorId).get();
            movieActor.setActor(actor);
            movieActors.add(movieActor);
        }

        return movieActors;
    }
}
