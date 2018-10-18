package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class RowTest {

    private static Row row1;
    private static Row row2;
    private static Row row3;

    @BeforeClass
    public static void setUpBeforeClass() {
        row1 = new Row();
        row1.setId(1);
        row1.setNumber(10);
        setSeatList(row1);

        row2 = new Row();
        row2.setId(1);
        row2.setNumber(10);
        setSeatList(row2);

        row3 = new Row();
        row3.setId(6);
        row3.setNumber(13);
        setSeatList(row3);
    }

    private static void setSeatList(Row row) {
        List<Seat> seatList = new ArrayList<>();
        Seat seat = new Seat();
        seat.setId(1);
        seat.setClientCount(8);
        seat.setNumber(10);
        seat.setPrice(10);
        seatList.add(seat);
        row.setSeats(new HashSet<>(seatList));
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(row1.equals(row2) && row2.equals(row1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(row2.equals(row3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(row1.equals(row2), row1.equals(row2));
        row1.setNumber(66);
        assertFalse(row1.equals(row2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, row1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(row1.equals(row1));
    }

    @Test
    public void testHashCode() {
        assertTrue(row1.hashCode() == row2.hashCode());
    }
}
