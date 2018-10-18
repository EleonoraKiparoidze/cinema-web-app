package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.authorization.Member;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    private static Comment comment1;
    private static Comment comment2;
    private static Comment comment3;

    @BeforeClass
    public static void setUpBeforeClass() {
        comment1 = new Comment();
        comment2 = new Comment();
        comment3 = new Comment();

        comment1.setId(1);
        comment1.setMessage("First");
        comment1.setUser(setUpMember());
        comment2.setId(1);
        comment2.setMessage("First");
        comment2.setUser(setUpMember());

        comment3.setId(5);
        comment3.setMessage("NotFirst");
        comment3.setUser(setUpMember());
    }

    private static Member setUpMember() {
        Member member = new Member();
        member.setId(2);
        member.setName("User1");
        member.setEmail("user@email.com");
        return member;
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(comment1.equals(comment2) && comment2.equals(comment1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(comment2.equals(comment3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(comment1.equals(comment2), comment1.equals(comment2));
        comment1.setMessage("Nice");
        assertFalse(comment1.equals(comment2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, comment1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(comment1.equals(comment1));
    }

    @Test
    public void testHashCode() {
        assertTrue(comment1.hashCode() == comment2.hashCode());
    }
}
