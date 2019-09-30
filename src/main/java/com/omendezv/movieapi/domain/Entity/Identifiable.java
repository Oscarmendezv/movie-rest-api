package com.omendezv.movieapi.domain.Entity;

public interface Identifiable extends org.springframework.hateoas.Identifiable<Long> {
    public Long getId();
}
