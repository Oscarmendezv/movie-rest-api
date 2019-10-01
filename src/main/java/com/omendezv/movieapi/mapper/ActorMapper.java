package com.omendezv.movieapi.mapper;

import com.omendezv.movieapi.DTO.ActorDTO;
import com.omendezv.movieapi.DTO.MovieDTO;
import com.omendezv.movieapi.repository.Entity.Actor;
import com.omendezv.movieapi.repository.Entity.MovieActor;
import com.omendezv.movieapi.repository.IActorDAO;
import com.omendezv.movieapi.repository.IMovieActorDAO;
import com.omendezv.movieapi.repository.IMovieDAO;
import com.omendezv.movieapi.service.MovieService;
import com.omendezv.movieapi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActorMapper {

    private IMovieActorDAO movieActorDAO;
    private DateUtils dateUtils;

    @Autowired
    public ActorMapper(IMovieActorDAO movieActorDAO, DateUtils dateUtils) {
        this.movieActorDAO = movieActorDAO;
        this.dateUtils = dateUtils;
    }

    public ActorDTO actorEntityToDTO(Actor actor, boolean includeMovies) {
        ActorDTO actorDTO = new ActorDTO();

        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        if (actor.getDateOfBirth() != null) actorDTO.setDateOfBirth(dateUtils.getFormatter().format(actor.getDateOfBirth()));
        if (actor.getDateOfDeath() != null) actorDTO.setDateOfDeath(dateUtils.getFormatter().format(actor.getDateOfDeath().toString()));
        if (includeMovies) actorDTO.setMovieAppearances(setMoviesToActorDTO(actorDTO));

        return actorDTO;
    }

    public Actor actorDTOtoEntity(ActorDTO actorDTO) {
        Actor actor = new Actor();
        actor.setId(actorDTO.getId());
        actor.setName(actorDTO.getName());
        try {
            if (actorDTO.getDateOfBirth() != null)
                actor.setDateOfBirth(dateUtils.getFormatter().parse(actorDTO.getDateOfBirth()));
            if (actorDTO.getDateOfDeath() != null)
                actor.setDateOfDeath(dateUtils.getFormatter().parse(actorDTO.getDateOfDeath()));
        } catch (ParseException e) {}

        return actor;
    }

    public List<ActorDTO> processActorListToActorDTO(List<Actor> actorList) {
        return actorList.stream().map(actor -> actorEntityToDTO(actor, true)).collect(Collectors.toList());
    }

    public List<Long> setMoviesToActorDTO(ActorDTO actorDTO) {
        List<Long> movieAppearances = new ArrayList<>();
        List<MovieActor> movieActorEntity = movieActorDAO.findByMovieActorIdActorId(actorDTO.getId());

        movieActorEntity.forEach(movieActor -> {
            movieAppearances.add(movieActor.getMovieActorId().getMovieId());
        });

        return movieAppearances;
    }
}
