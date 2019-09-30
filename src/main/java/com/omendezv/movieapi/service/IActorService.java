package com.omendezv.movieapi.service;

import com.omendezv.movieapi.domain.DTO.ActorDTO;
import com.omendezv.movieapi.domain.DTO.MovieDTO;
import com.omendezv.movieapi.domain.Entity.Actor;

import java.util.List;

public interface IActorService {

    List<ActorDTO> findAllActors();

    ActorDTO findActorById(Long id);

    List<ActorDTO> findActorsByName(String name);

    ActorDTO createActor(Actor actor);

    ActorDTO updateActor(Actor actor);

    void deleteActorById(Long id);

    ActorDTO actorEntityToDTO(Actor actor, boolean includeMovies);

    List<MovieDTO> setMoviesToActorDTO(ActorDTO actorDTO);

    List<ActorDTO> processActorListToActorDTO(List<Actor> actorList);

    Actor actorDTOtoEntity(ActorDTO actorDTO);
}
