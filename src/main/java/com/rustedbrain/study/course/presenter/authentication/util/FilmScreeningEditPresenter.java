package com.rustedbrain.study.course.presenter.authentication.util;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.CinemaService;

public class FilmScreeningEditPresenter {

	private final CinemaService cinemaService;

	public FilmScreeningEditPresenter(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}

	public void buttonAddNewFilmScreeningClicked(Optional<Movie> movie, Optional<Cinema> cinema,
			LocalDateTime startDate, LocalDateTime endDate) {
		cinemaService.createFilmScreening(movie.get(), cinema.get(), Date.valueOf(startDate.toLocalDate()),
				Date.valueOf(endDate.toLocalDate()));
	}

	public void buttonSaveFilmScreeningClicked(FilmScreening selectedFilmScreening, Movie movie, Cinema cinema,
			LocalDateTime startDate, LocalDateTime endDate) {
		cinemaService.editFilmScreening(selectedFilmScreening.getId(), movie, cinema,
				Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()),
				Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()));
	}

	public void buttonDeleteFilmScreeningClicked(long id) {
		cinemaService.deleteFilmScreening(id);
	}

	public void buttonDeleteFilmScreeningEventClicked(long id) {
		cinemaService.deleteFilmScreeningEvent(id);
	}

	public void buttonAddNewFilmScreeningEventClicked(FilmScreening filmScreening, CinemaHall cinemaHall,
			java.sql.Date date, Time time) {
		cinemaService.createFilmScreeningEvent(filmScreening, cinemaHall, date, time);
	}

}
