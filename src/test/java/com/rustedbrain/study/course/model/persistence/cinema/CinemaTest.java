package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CinemaTest {

    private static Cinema cinema1;
    private static Cinema cinema2;
    private static Cinema cinema3;

    @BeforeClass
    public static void setUpBeforeClass() {
        cinema1 = new Cinema();
        cinema2 = new Cinema();
        cinema3 = new Cinema();
        cinema1.setId(1);
        cinema1.setName("CinemaName");
        setUpCinemaCity(cinema1);
        cinema2.setId(1);
        cinema2.setName("CinemaName");
        setUpCinemaCity(cinema2);
        cinema3.setId(5);
        cinema3.setName("OtherCinemaName");
        setUpCinemaCity(cinema3);
    }

    private static void setUpCinemaCity(Cinema cinema) {
        City city = new City();
        city.setId(5);
        city.setName("CityName");
        cinema.setCity(city);
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(cinema1.equals(cinema2) && cinema2.equals(cinema1));
    }

    @Test
    public void testNotEqualsSymmetry() {
        assertFalse(cinema2.equals(cinema3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(cinema1.equals(cinema2), cinema1.equals(cinema2));
        cinema1.setName("OtherName");
        assertFalse(cinema1.equals(cinema2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, cinema1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(cinema1.equals(cinema1));
    }

    @Test
    public void testHashCode() {
        assertTrue(cinema1.hashCode() == cinema2.hashCode());
    }
}
