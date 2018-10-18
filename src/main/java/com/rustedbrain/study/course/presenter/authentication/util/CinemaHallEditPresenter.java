package com.rustedbrain.study.course.presenter.authentication.util;

import java.util.Optional;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.util.PageNavigator;

public class CinemaHallEditPresenter {

	private final CinemaService cinemaService;

	public CinemaHallEditPresenter(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}

	public void buttonDeleteCinemaHallClicked(long id) {
		cinemaService.deleteCinemaHall(id);
	}

	public void buttonSaveCinemaHallClicked(CinemaHall selectedCinemaHall, String newCinemaHallName, Cinema newCinema) {
		cinemaService.editCinemaHall(selectedCinemaHall, newCinemaHallName, newCinema);

	}

	public void buttonAddNewCinemaHallClicked(String cinemaHallName, Optional<Cinema> cinema) {
		long cinamaHallId = cinemaService.createCinemaHall(cinemaHallName, cinema.get());
		new PageNavigator().navigateToCinemaHallConstructorView(cinamaHallId);
	}

	public void buttonChangeCinemaHallSeatsClicked(CinemaHall selectedCinemaHall) {
		new PageNavigator().navigateToCinemaHallConstructorView(selectedCinemaHall.getId());
	}
}
