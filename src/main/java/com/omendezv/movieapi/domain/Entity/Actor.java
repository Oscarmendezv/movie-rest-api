package com.omendezv.movieapi.domain.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name="actors")
public class Actor implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long actorId;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_death")
    private Date dateOfDeath;

    @OneToMany(
            mappedBy = "actor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<MovieActor> movieAppearances;

    @Override
    public Long getId() {
        return actorId;
    }

    public void setId(Long id) {
        this.actorId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date yearOfBirth) {
        this.dateOfBirth = yearOfBirth;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date yearOfDeath) {
        this.dateOfDeath = yearOfDeath;
    }

    public Set<MovieActor> getMovieAppearances() {
        return movieAppearances;
    }

    public void setMovieAppearances(Set<MovieActor> movieAppearances) {
        this.movieAppearances = movieAppearances;
    }
}
