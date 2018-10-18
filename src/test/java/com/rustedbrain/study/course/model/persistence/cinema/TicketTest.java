package com.rustedbrain.study.course.model.persistence.cinema;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class TicketTest {

    private static Ticket ticket1;
    private static Ticket ticket2;
    private static Ticket ticket3;

    @BeforeClass
    public static void setUpBeforeClass() {
        ticket1 = new Ticket();
        ticket2 = new Ticket();
        ticket1.setId(1);
        ticket1.setSeat(setUpSeat());
        setUpEvent(ticket1, new Time(10));
        ticket2.setId(1);
        ticket2.setSeat(setUpSeat());
        setUpEvent(ticket2, new Time(10));

        ticket3 = new Ticket();
        ticket3.setId(6);
        ticket3.setSeat(setUpSeat());
        setUpEvent(ticket3, new Time(23));
    }

    private static Seat setUpSeat() {
        Seat seat = new Seat();
        seat.setId(11);
        seat.setClientCount(8);
        seat.setNumber(10);
        seat.setPrice(10);
        return seat;
    }

    private static void setUpEvent(Ticket ticket, Time time) {
        FilmScreeningEvent filmScreeningEvent = new FilmScreeningEvent();
        filmScreeningEvent.setId(2);
        filmScreeningEvent.setTime(time);
        setUpCinemaHall(filmScreeningEvent);
        ticket.setEvent(filmScreeningEvent);
    }

    private static void setUpCinemaHall(FilmScreeningEvent filmScreeningEvent) {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setId(1);
        cinemaHall.setName("CinemaHallName");
        setUpRowList(cinemaHall);
        filmScreeningEvent.setCinemaHall(cinemaHall);
    }

    private static void setUpRowList(CinemaHall cinemaHall) {
        List<Row> rowList = new ArrayList<>();
        Row row = new Row();
        rowList.add(setUpSeatList(row));
        row.setId(1);
        row.setNumber(9);
        cinemaHall.setRows(rowList);
    }

    private static Row setUpSeatList(Row row) {
        List<Seat> seatList = new ArrayList<>();
        Seat seat = new Seat();
        seat.setId(1);
        seat.setClientCount(11);
        seat.setNumber(13);
        seat.setPrice(10);
        seatList.add(setUpSeat());
        row.setSeats(new HashSet<>(seatList));
        return row;
    }

    @Test
    public void testSymmetryEquals() {
        assertTrue(ticket1.equals(ticket2) && ticket2.equals(ticket1));
    }

    @Test
    public void testNotEquals() {
        assertFalse(ticket2.equals(ticket3));
    }

    @Test
    public void testEqualsConsistency() {
        assertEquals(ticket1.equals(ticket2), ticket1.equals(ticket2));
        setUpEvent(ticket1, new Time(22));
        assertFalse(ticket1.equals(ticket2));
    }

    @Test
    public void testEqualsComparisonOfNull() {
        assertEquals(false, ticket1.equals(null));
    }

    @Test
    public void testEqualsReflectivity() {
        assertTrue(ticket1.equals(ticket1));
    }

    @Test
    public void testHashCode() {
        assertTrue(ticket1.hashCode() == ticket2.hashCode());
    }
}
