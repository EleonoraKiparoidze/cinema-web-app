package com.rustedbrain.study.course.model.persistence.cinema;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;

@Entity
@Table(name = "filmScreeningEvent")
public class FilmScreeningEvent extends DatabaseEntity {
	private static final long serialVersionUID = -4544491762916448238L;
	@ManyToOne
	@JoinColumn(name = "filmScreeningId")
	private FilmScreening filmScreening;
	@OneToMany(mappedBy = "event")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<Ticket> tickets;
	@Column(name = "date")
	private Date date;
	@Column(name = "time")
	private Time time;
	@ManyToOne
	private CinemaHall cinemaHall;

	public FilmScreeningEvent(CinemaHall cinemaHall, Date date, Time time) {
		this.cinemaHall = cinemaHall;
		this.date = date;
		this.time = time;
	}

	public FilmScreeningEvent() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	public FilmScreening getFilmScreening() {
		return filmScreening;
	}

	public void setFilmScreening(FilmScreening filmScreening) {
		this.filmScreening = filmScreening;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public CinemaHall getCinemaHall() {
		return cinemaHall;
	}

	public void setCinemaHall(CinemaHall cinemaHall) {
		this.cinemaHall = cinemaHall;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		if ( !super.equals(o) )
			return false;

		FilmScreeningEvent that = (FilmScreeningEvent) o;

		if ( !date.equals(that.date) )
			return false;
		return time.equals(that.time);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + date.hashCode();
		result = 31 * result + time.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "FilmScreeningEvent{" + "time=" + time + ", cinemaHall=" + cinemaHall + '}';
	}
}
