package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class FilmScreeningEventTest {

    private static FilmScreeningEvent filmScreeningEvent1;
    private static FilmScreeningEvent filmScreeningEvent2;
    private static FilmScreeningEvent filmScreeningEvent3;

    @BeforeClass
    public static void setUpBeforeClass() {
        filmScreeningEvent1 = new FilmScreeningEvent();
        filmScreeningEvent2 = new FilmScreeningEvent();
        filmScreeningEvent1.setId(1);
        filmScreeningEvent1.setTime(new Time(10));
        setUpCinemaHall(filmScreeningEvent1);
        filmScreeningEvent2.setId(1);
        filmScreeningEvent2.setTime(new Time(10));
        setUpCinemaHall(filmScreeningEvent2);

        filmScreeningEvent3 = new FilmScreeningEvent();
        filmScreeningEvent3.setId(3);
        filmScreeningEvent3.setTime(new Time(20));
        setUpCinemaHall(filmScreeningEvent3);
    }

    private static void setUpCinemaHall(FilmScreeningEvent filmScreeningEvent) {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setId(2);
        cinemaHall.setName("CinemaHallName");
        setUpRowList(cinemaHall);
        filmScreeningEvent.setCinemaHall(cinemaHall);
    }

    private static void setUpRowList(CinemaHall cinemaHall) {
        List<Row> rowList = new ArrayList<>();
        Row row = new Row();
        rowList.add(setUpSeatList(row));
        row.setId(1);
        row.setNumber(8);
        cinemaHall.setRows(rowList);
    }

    private static Row setUpSeatList(Row row) {
        List<Seat> seatList = new ArrayList<>();
        Seat seat = new Seat();
        seat.setId(1);
        seat.setClientCount(10);
        seat.setNumber(13);
        seat.setPrice(10);
        seatList.add(seat);
        row.setSeats(new HashSet<>(seatList));
        return row;
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(filmScreeningEvent1.equals(filmScreeningEvent2) && filmScreeningEvent2.equals(filmScreeningEvent1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(filmScreeningEvent2.equals(filmScreeningEvent3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(filmScreeningEvent1.equals(filmScreeningEvent2), filmScreeningEvent1.equals(filmScreeningEvent2));
        filmScreeningEvent1.setTime(new Time(21));
        assertFalse(filmScreeningEvent1.equals(filmScreeningEvent2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, filmScreeningEvent1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(filmScreeningEvent1.equals(filmScreeningEvent1));
    }

    @Test
    public void testHashCode() {
        assertTrue(filmScreeningEvent1.hashCode() == filmScreeningEvent2.hashCode());
    }
}
