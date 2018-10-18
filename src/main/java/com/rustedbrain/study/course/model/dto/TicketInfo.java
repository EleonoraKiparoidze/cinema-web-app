package com.rustedbrain.study.course.model.dto;

import java.sql.Time;
import java.util.Date;

import com.rustedbrain.study.course.model.persistence.cinema.Ticket;

public class TicketInfo {

	private long id;
	private String movie;
	private Date date;
	private Time time;
	private String hall;
	private int row;
	private int seat;
	private double price;
	private boolean reserved;

	public TicketInfo(Ticket ticket) {
		this(ticket.getId(), ticket.getEvent().getFilmScreening().getMovie().getLocalizedName(),
				ticket.getEvent().getDate(), ticket.getEvent().getTime(), ticket.getEvent().getCinemaHall().getName(),
				ticket.getSeat().getRow().getNumber(), ticket.getSeat().getNumber(), ticket.getSeat().getPrice(),
				ticket.isReserved());
	}

	public TicketInfo(long id, String movie, Date date, Time time, String hall, int row, int seat, double price,
			boolean reserved) {
		this.id = id;
		this.movie = movie;
		this.date = date;
		this.time = time;
		this.hall = hall;
		this.row = row;
		this.seat = seat;
		this.price = price;
		this.reserved = reserved;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getHall() {
		return hall;
	}

	public void setHall(String hall) {
		this.hall = hall;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
