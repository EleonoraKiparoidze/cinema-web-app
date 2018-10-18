package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class CinemaHallTest {

    private static CinemaHall cinemaHall1;
    private static CinemaHall cinemaHall2;
    private static CinemaHall cinemaHall3;

    @BeforeClass
    public static void setUpBeforeClass() {
        cinemaHall1 = new CinemaHall();
        cinemaHall1.setId(1);
        cinemaHall1.setName("CinemaHallName");
        setUpRowList(cinemaHall1);

        cinemaHall2 = new CinemaHall();
        cinemaHall2.setId(1);
        cinemaHall2.setName("CinemaHallName");
        setUpRowList(cinemaHall2);

        cinemaHall3 = new CinemaHall();
        cinemaHall3.setId(5);
        cinemaHall3.setName("OtherCinemaHallName");
        setUpRowList(cinemaHall3);
    }

    private static void setUpRowList(CinemaHall cinemaHall) {
        List<Row> rowList = new ArrayList<>();
        Row row = new Row();
        rowList.add(setUpSeatList(row));
        row.setId(1);
        row.setNumber(10);
        cinemaHall.setRows(rowList);
    }

    private static Row setUpSeatList(Row row) {
        List<Seat> seatList = new ArrayList<>();
        Seat seat = new Seat();
        seat.setId(1);
        seat.setClientCount(13);
        seat.setNumber(13);
        seat.setPrice(10);
        seatList.add(seat);
        row.setSeats(new HashSet<>(seatList));
        return  row;
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(cinemaHall1.equals(cinemaHall2) && cinemaHall2.equals(cinemaHall1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(cinemaHall2.equals(cinemaHall3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(cinemaHall1.equals(cinemaHall2), cinemaHall1.equals(cinemaHall2));
        cinemaHall1.setName("OtherCinemaHallName");
        assertFalse(cinemaHall1.equals(cinemaHall2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, cinemaHall1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(cinemaHall1.equals(cinemaHall1));
    }

    @Test
    public void testHashCode() {
        assertTrue(cinemaHall1.hashCode() == cinemaHall2.hashCode());
    }
}
