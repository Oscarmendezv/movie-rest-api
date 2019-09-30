package com.omendezv.movieapi.domain;

import com.omendezv.movieapi.domain.Entity.Movie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MovieTests {

    Movie movie;

    @Before
    public void setUp() {
        movie = new Movie();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterYear() {
        movie.setYear(20000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLowerYear() {
        movie.setYear(1880);
    }

    @Test
    public void testCorrectYear() {
        movie.setYear(2019);
        Assert.assertEquals(movie.getYear(), 2019);
    }
}
