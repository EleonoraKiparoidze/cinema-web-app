package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "row")
public class Row extends DatabaseEntity implements Comparable<Row> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -313572066138447338L;
	@Column(name = "number", nullable = false)
	private int number;
	@ManyToOne(fetch = FetchType.LAZY)
	private CinemaHall cinemaHall;
	@OneToMany(mappedBy = "row", fetch = FetchType.EAGER)
	@Cascade({ CascadeType.ALL })
	private Set<Seat> seats = new HashSet<>();

	public Row(int number, Set<Seat> seats) {
		this.number = number;
		this.seats = seats;
	}

	public Row() {
	}

	public CinemaHall getCinemaHall() {
		return cinemaHall;
	}

	public void setCinemaHall(CinemaHall cinemaHall) {
		this.cinemaHall = cinemaHall;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Set<Seat> getSeats() {
		return seats;
	}

	public void setSeats(Set<Seat> seats) {
		this.seats = seats;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;

		Row row = (Row) o;

		return number == row.number;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + number;
		return result;
	}

	@Override
	public String toString() {
		return "Row{" + "number=" + number + ", seats=" + seats + '}';
	}

	@Override
	public int compareTo(Row o) {
		return Integer.compare(this.number, o.number);
	}
}
