package com.omendezv.movieapi.service;

import com.omendezv.movieapi.domain.DTO.ActorDTO;
import com.omendezv.movieapi.domain.Entity.Actor;
import com.omendezv.movieapi.repository.IActorDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ActorServiceTests {

    @Mock
    private IActorDAO actorDAO;

    @InjectMocks
    private ActorService actorService;

    private List<Actor> actors;
    private Actor generatedActor1;
    private Actor generatedActor2;
    private Actor generatedActor3;


    @Before
    public void init(){
        actors = new ArrayList<>();

        generatedActor1 = new Actor();
        generatedActor2 = new Actor();
        generatedActor3 = new Actor();

        generatedActor1.setId(1L);
        generatedActor1.setName("ActorTest1");
        generatedActor1.setDateOfBirth(new Date(1995-03-04));

        generatedActor2.setId(2L);
        generatedActor2.setName("ActorTest2");
        generatedActor2.setDateOfBirth(new Date(1995-03-04));

        generatedActor3.setId(3L);
        generatedActor3.setName("ActorTest3");
        generatedActor3.setDateOfBirth(new Date(1995-03-04));

        actors.add(generatedActor1);
        actors.add(generatedActor2);
        actors.add(generatedActor3);
    }

    @Test
    public void findAllTest() {
        Mockito.when(actorDAO.findAll()).thenReturn(actors);
        List<ActorDTO> obtainedActors = actorService.findAllActors();

        assertNotNull(obtainedActors);
        assertEquals(actors, obtainedActors);
    }

    @Test
    public void findActorByIdTest() {
        Mockito.when(actorDAO.findById(1L)).thenReturn(Optional.of(generatedActor1));
        ActorDTO obtainedActor = actorService.findActorById(1L);

        assertNotNull(obtainedActor);
        assertEquals(generatedActor1, obtainedActor);
    }

}