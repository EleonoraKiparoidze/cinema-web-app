package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActorTest {

    private static Actor actor1;
    private static Actor actor2;
    private static Actor actor3;

    @BeforeClass
    public static void setUpBeforeClass() {
        actor1 = new Actor();
        actor1.setId(1);
        actor1.setName("ActorName");
        actor1.setSurname("ActorSurname");

        actor2 = new Actor();
        actor2.setId(1);
        actor2.setName("ActorName");
        actor2.setSurname("ActorSurname");

        actor3 = new Actor();
        actor3.setId(3);
        actor3.setName("OtherActorName");
        actor3.setSurname("ActorSurname");

    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(actor1.equals(actor2) && actor2.equals(actor1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(actor2.equals(actor3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(actor1.equals(actor2), actor1.equals(actor2));
        actor1.setName("OtherName");
        assertFalse(actor1.equals(actor2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, actor1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(actor1.equals(actor1));
    }

    @Test
    public void testHashCode() {
        assertTrue(actor1.hashCode() == actor2.hashCode());
    }

}