package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CityTest {

    private static City city1;
    private static City city2;
    private static City city3;

    @BeforeClass
    public static void setUpBeforeClass() {
        city1 = new City();
        city2 = new City();
        city3 = new City();
        city1.setId(1);
        city1.setName("CityName");
        city2.setId(1);
        city2.setName("CityName");
        city3.setId(5);
        city3.setName("OtherCityName");
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(city1.equals(city2) && city2.equals(city1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(city2.equals(city3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(city1.equals(city2), city1.equals(city2));
        city1.setName("OtherCityName");
        assertFalse(city1.equals(city2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, city1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(city1.equals(city1));
    }

    @Test
    public void testHashCode() {
        assertTrue(city1.hashCode() == city2.hashCode());
    }
}
