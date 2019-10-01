package com.omendezv.movieapi.service.Imp;

import com.omendezv.movieapi.DTO.ActorDTO;
import com.omendezv.movieapi.mapper.ActorMapper;
import com.omendezv.movieapi.repository.Entity.Actor;
import com.omendezv.movieapi.exceptions.ActorInsertException;
import com.omendezv.movieapi.exceptions.ActorNotFoundException;
import com.omendezv.movieapi.repository.IActorDAO;
import com.omendezv.movieapi.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ActorServiceImp implements ActorService {

    private IActorDAO actorDAO;
    private ActorMapper actorMapper;

    @Autowired
    public ActorServiceImp(IActorDAO actorDAO, ActorMapper actorMapper) {
        this.actorDAO = actorDAO;
        this.actorMapper = actorMapper;
    }

    public List<ActorDTO> findAllActors() {
        return actorMapper.processActorListToActorDTO(actorDAO.findAll());
    }

    public ActorDTO findActorById(Long id) {
        return actorMapper.actorEntityToDTO(actorDAO.findById(id).orElseThrow(() -> new ActorNotFoundException()), true);
    }

    public List<ActorDTO> findActorsByName(String name) {
        return actorMapper.processActorListToActorDTO(actorDAO.findActorsByName(name));
    }

    @Transactional
    public ActorDTO createActor(Actor actor) {
        if (actorDAO.findById(actor.getId()).isPresent())
            throw new ActorInsertException();

        return actorMapper.actorEntityToDTO(actorDAO.save(actor), true);
    }

    @Transactional
    public ActorDTO updateActor(Actor updatedActor) {
        ActorDTO currentActor = findActorById(updatedActor.getId());
        if (currentActor.equals(updatedActor))
            return currentActor;

        return actorMapper.actorEntityToDTO(actorDAO.save(updatedActor), true);
    }

    @Transactional
    public void deleteActorById(Long id) {
        findActorById(id);
        actorDAO.deleteById(id);
    }
}
