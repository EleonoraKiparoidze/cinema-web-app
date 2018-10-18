package com.rustedbrain.study.course.model.persistence.cinema;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;

@Entity
@Table(name = "cinemaHall")
public class CinemaHall extends DatabaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5269490138494694336L;
	@Column(name = "name", length = 64, nullable = false)
	private String name;
	@OneToMany(mappedBy = "cinemaHall")
	@Cascade({ CascadeType.ALL })
	private List<Row> rows = new LinkedList<>();
	@ManyToOne
	private Cinema cinema;
	@OneToMany(mappedBy = "cinemaHall")
	@Cascade({ CascadeType.ALL })
	private Set<FilmScreeningEvent> filmScreeningEvents = new HashSet<>();

	public CinemaHall(String name) {
		this.name = name;
	}

	public CinemaHall() {
	}

	public Set<FilmScreeningEvent> getFilmScreeningEvents() {
		return filmScreeningEvents;
	}

	public void setFilmScreeningEvents(Set<FilmScreeningEvent> filmScreeningEvents) {
		this.filmScreeningEvents = filmScreeningEvents;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;

		CinemaHall that = (CinemaHall) o;

		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "CinemaHall{" + "name='" + name + '\'' + ", rows=" + rows + '}';
	}
}
