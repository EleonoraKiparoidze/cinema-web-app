package com.rustedbrain.study.course.model.persistence.authorization;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestUser {

    private static User user1;
    private static User user2;
    private static User user3;

    @BeforeClass
    public static void setUpBeforeClass() {
        user1 = new User();
        user1.setEmail("user@gmail.com");
        user1.setId(1);

        user2 = new User();
        user2.setEmail("user@gmail.com");
        user2.setId(1);

        user3 = new User();
        user3.setEmail("test@gmail.com");
        user3.setId(3);
    }

    @Test
    public void testEqualsSymmetry() {
        assertTrue(user1.equals(user2) && user2.equals(user1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(user2.equals(user3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(user1.equals(user2), user1.equals(user2));
        user1.setEmail("consistency@test.com");
        assertFalse(user1.equals(user2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, user1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(user1.equals(user1));
    }

    @Test
    public void testHashCode() {
        assertTrue(user1.hashCode() == user2.hashCode());
    }

    @Test
    public void testGetSetName() {
        user1.setSurname("Surname");
        assertEquals("Surname", user1.getSurname());
    }
}
