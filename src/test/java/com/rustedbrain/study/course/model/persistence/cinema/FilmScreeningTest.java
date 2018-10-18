package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class FilmScreeningTest {

    private static FilmScreening filmScreening1;
    private static FilmScreening filmScreening2;
    private static FilmScreening filmScreening3;

    @BeforeClass
    public static void setUpBeforeClass() {
        filmScreening1 = new FilmScreening();
        filmScreening2 = new FilmScreening();
        filmScreening1.setId(1);
        filmScreening1.setMovie(setUpMovie("OriginalName"));
        filmScreening2.setId(1);
        filmScreening2.setMovie(setUpMovie("OriginalName"));

        filmScreening3 = new FilmScreening();
        filmScreening3.setId(4);
        filmScreening3.setMovie(setUpMovie("OtherName"));
    }

    private static Movie setUpMovie(String originalName) {
        Movie movie = new Movie();
        movie.setId(6);
        movie.setOriginalName(originalName);
        movie.setLocalizedName("LocalizedNameOne");
        movie.setReleaseDate(Date.valueOf(LocalDate.of(2017, 3, 3)));
        return movie;
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(filmScreening1.equals(filmScreening2) && filmScreening2.equals(filmScreening1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(filmScreening2.equals(filmScreening3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(filmScreening1.equals(filmScreening2), filmScreening1.equals(filmScreening2));
        filmScreening1.setMovie(setUpMovie("TestName"));
        assertFalse(filmScreening1.equals(filmScreening2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, filmScreening1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(filmScreening1.equals(filmScreening1));
    }

    @Test
    public void testHashCode() {
        assertTrue(filmScreening1.hashCode() == filmScreening2.hashCode());
    }
}
