package com.omendezv.movieapi.repository;

import com.omendezv.movieapi.repository.Entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IActorDAO extends JpaRepository<Actor, Long> {

    @Query("SELECT a.actorId, a.name FROM Actor a WHERE a.name LIKE CONCAT('%',:name,'%')")
    List<Actor> findActorsByName(@Param("name") String name);

}
