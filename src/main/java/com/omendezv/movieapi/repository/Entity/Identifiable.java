package com.omendezv.movieapi.repository.Entity;

public interface Identifiable extends org.springframework.hateoas.Identifiable<Long> {
    public Long getId();
}
