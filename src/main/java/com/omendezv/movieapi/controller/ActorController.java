package com.omendezv.movieapi.controller;

import com.omendezv.movieapi.DTO.ActorDTO;
import com.omendezv.movieapi.repository.Entity.Actor;

import java.util.List;

public interface ActorController {

    List<ActorDTO> getAllActors();

    ActorDTO getActorById(Long id);

    List<ActorDTO> getActorsByName( String name);

    ActorDTO createActor(Actor actor);

    ActorDTO updateActor(Long id, Actor actor);
}
