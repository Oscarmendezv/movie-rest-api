package com.omendezv.movieapi.service.Imp;

import com.omendezv.movieapi.DTO.ActorDTO;
import com.omendezv.movieapi.DTO.MovieDTO;
import com.omendezv.movieapi.repository.Entity.Actor;
import com.omendezv.movieapi.repository.Entity.MovieActor;
import com.omendezv.movieapi.exceptions.ActorInsertException;
import com.omendezv.movieapi.exceptions.ActorNotFoundException;
import com.omendezv.movieapi.repository.IActorDAO;
import com.omendezv.movieapi.repository.IMovieActorDAO;
import com.omendezv.movieapi.repository.IMovieDAO;
import com.omendezv.movieapi.service.ActorService;
import com.omendezv.movieapi.service.MovieService;
import com.omendezv.movieapi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorServiceImp implements ActorService {

    private IMovieDAO movieDAO;
    private IMovieActorDAO movieActorDAO;
    private IActorDAO actorDAO;
    private MovieService movieService;
    private DateUtils dateUtils;

    @Autowired
    public ActorServiceImp(IMovieDAO movieDAO, IMovieActorDAO movieActorDAO, IActorDAO actorDAO, MovieService movieService, DateUtils dateUtils) {
        this.movieDAO = movieDAO;
        this.movieActorDAO = movieActorDAO;
        this.actorDAO = actorDAO;
        this.movieService = movieService;
        this.dateUtils = dateUtils;
    }

    public List<ActorDTO> findAllActors() {
        return processActorListToActorDTO(actorDAO.findAll());
    }

    public ActorDTO findActorById(Long id) {
        return actorEntityToDTO(actorDAO.findById(id).orElseThrow(() -> new ActorNotFoundException()), true);
    }

    public List<ActorDTO> findActorsByName(String name) {
        return processActorListToActorDTO(actorDAO.findActorsByName(name));
    }

    @Transactional
    public ActorDTO createActor(Actor actor) {
        if (actorDAO.findById(actor.getId()).isPresent())
            throw new ActorInsertException();

        return actorEntityToDTO(actorDAO.save(actor), true);
    }

    @Transactional
    public ActorDTO updateActor(Actor updatedActor) {
        ActorDTO currentActor = findActorById(updatedActor.getId());
        if (currentActor.equals(updatedActor))
            return currentActor;

        return actorEntityToDTO(actorDAO.save(updatedActor), true);
    }

    @Transactional
    public void deleteActorById(Long id) {
        findActorById(id);
        actorDAO.deleteById(id);
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

    public List<MovieDTO> setMoviesToActorDTO(ActorDTO actorDTO) {
        List<MovieDTO> movieAppearances = new ArrayList<>();
        List<MovieActor> movieActorEntity = movieActorDAO.findByMovieActorIdActorId(actorDTO.getId());

        movieActorEntity.forEach(movieActor -> {
            movieAppearances.add(movieService.movieEntityToDTO(movieDAO.findById(movieActor.getMovieActorId().getMovieId()).get(), false));
        });

        return movieAppearances;
    }

    public List<ActorDTO> processActorListToActorDTO(List<Actor> actorList) {
        return actorList.stream().map(actor -> actorEntityToDTO(actor, true)).collect(Collectors.toList());
    }
}
