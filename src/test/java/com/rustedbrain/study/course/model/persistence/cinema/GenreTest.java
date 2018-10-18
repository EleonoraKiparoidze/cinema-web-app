package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class GenreTest {

    private static Genre genre1;
    private static Genre genre2;
    private static Genre genre3;

    @BeforeClass
    public static void setUpBeforeClass() {
        genre1 = new Genre();
        genre2 = new Genre();
        genre1.setId(1);
        genre1.setName("Horror");
        genre2.setId(1);
        genre2.setName("Horror");

        genre3 = new Genre();
        genre3.setId(5);
        genre3.setName("Drama");
    }

    @Test
    public void testSymmetryEquals() {
        assertEquals(genre1.equals(genre2), genre2.equals(genre1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(genre2.equals(genre3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(genre1.equals(genre2), genre1.equals(genre2));
        genre1.setName("Melodrama");
        assertFalse(genre1.equals(genre2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, genre1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(genre1.equals(genre1));
    }

    @Test
    public void testHashCode() {
        assertEquals(genre1.hashCode(), genre2.hashCode());
    }
}
