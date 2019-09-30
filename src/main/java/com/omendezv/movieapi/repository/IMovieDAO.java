package com.omendezv.movieapi.repository;

import com.omendezv.movieapi.domain.Entity.Movie;
import com.omendezv.movieapi.domain.enums.MovieGenresEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMovieDAO extends JpaRepository<Movie, Long> {

    List<Movie> findByMovieGenre(MovieGenresEnum genre);

    @Query("SELECT m FROM Movie m WHERE m.title LIKE CONCAT('%',:title,'%')")
    List<Movie> findMoviesByTitle(@Param("title") String title);
}
