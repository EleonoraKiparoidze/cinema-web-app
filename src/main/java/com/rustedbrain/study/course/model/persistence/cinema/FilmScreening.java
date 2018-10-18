package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "filmScreening")
public class FilmScreening extends DatabaseEntity {

	private static final long serialVersionUID = -4155594195767991639L;
	@ManyToOne
	private Cinema cinema;
	@ManyToOne
	private Movie movie;
	@Column(name = "startDate")
	private Date startDate;
	@OneToMany(mappedBy = "filmScreening")
	@Cascade({ CascadeType.ALL })
	private List<FilmScreeningEvent> filmScreeningEvents;
	@Column(name = "endDate")
	private Date endDate;
	@Column(name = "isAvailableToBuy")
	private boolean isAvailableToBuy;

	public boolean isAvailableToBuy() {
		return isAvailableToBuy;
	}

	public void setAvailableToBuy(boolean availableToBuy) {
		isAvailableToBuy = availableToBuy;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<FilmScreeningEvent> getFilmScreeningEvents() {
		return filmScreeningEvents;
	}

	public void setFilmScreeningEvents(List<FilmScreeningEvent> filmScreeningEvents) {
		this.filmScreeningEvents = filmScreeningEvents;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		if ( !super.equals(o) )
			return false;

		FilmScreening that = (FilmScreening) o;

		return (movie != null ? movie.equals(that.movie) : that.movie == null) && startDate.equals(that.startDate)
				&& endDate.equals(that.endDate);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (movie != null ? movie.hashCode() : 0);
		result = 31 * result + startDate.hashCode();
		result = 31 * result + endDate.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "FilmScreening{" + "movie=" + movie + ", startDate=" + startDate + ", filmScreeningEvents="
				+ filmScreeningEvents + ", endDate=" + endDate + '}';
	}
}
