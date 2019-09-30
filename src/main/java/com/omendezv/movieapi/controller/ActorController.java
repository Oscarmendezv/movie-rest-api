package com.omendezv.movieapi.controller;

import com.omendezv.movieapi.domain.DTO.ActorDTO;
import com.omendezv.movieapi.domain.Entity.Actor;
import com.omendezv.movieapi.service.IActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actors")
public class ActorController {

    @Autowired
    private IActorService actorService;

    @GetMapping
    public List<ActorDTO> getAllActors(){
        return actorService.findAllActors();
    }

    @GetMapping("/{id}")
    public ActorDTO getActorById(@PathVariable Long id) {
        return actorService.findActorById(id);
    }

    @GetMapping("/search/{name}")
    public List<ActorDTO> getActorsByName(@PathVariable String name) {
        return actorService.findActorsByName(name);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ActorDTO createActor(@RequestBody Actor actor) {
        return actorService.createActor(actor);
    }

    @PutMapping("/{id}")
    public ActorDTO updateActor(@PathVariable Long id, @RequestBody Actor actor) {
        return actorService.updateActor(actor);
    }
}
