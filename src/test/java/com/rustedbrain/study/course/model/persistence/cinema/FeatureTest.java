package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class FeatureTest {

    private static Feature feature1;
    private static Feature feature2;
    private static Feature feature3;

    @BeforeClass
    public static void setUpBeforeClass() {
        feature1 = new Feature();
        feature2 = new Feature();
        feature1.setId(1);
        feature1.setName("FeatureFilmName");
        feature1.setFeatureDescription("Description1");
        feature2.setId(1);
        feature2.setName("FeatureFilmName");
        feature2.setFeatureDescription("Description2");

        feature3 = new Feature();
        feature3.setId(5);
        feature3.setName("SomeFilmName");
        feature3.setFeatureDescription("Some information");
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(feature1.equals(feature2) && feature2.equals(feature1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(feature2.equals(feature3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(feature1.equals(feature2), feature1.equals(feature2));
        feature1.setName("OtherName");
        assertFalse(feature1.equals(feature2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, feature1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(feature1.equals(feature1));
    }

    @Test
    public void testHashCode() {
        assertTrue(feature1.hashCode() == feature2.hashCode());
    }
}
