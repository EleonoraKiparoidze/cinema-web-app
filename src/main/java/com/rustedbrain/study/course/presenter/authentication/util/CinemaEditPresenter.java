package com.rustedbrain.study.course.presenter.authentication.util;

import java.util.Set;

import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Feature;
import com.rustedbrain.study.course.service.CinemaService;

public class CinemaEditPresenter {
	private final CinemaService cinemaService;

	public CinemaEditPresenter(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}

	public void buttonSaveCinemaClicked(Cinema selectedCinema, String newCinemaName, City newCity,
			String newCinemaLocation, Manager newCinemaManager) {
		cinemaService.editCinema(selectedCinema, newCinemaName, newCity, newCinemaLocation, newCinemaManager);
	}

	public void buttonDeleteCinemaClicked(long id) {
		cinemaService.deleteCinema(id);
	}

	public void buttonAddNewCinemaClicked(City city, String cinemaName, String location, Manager manager) {
		cinemaService.createCinema(city, cinemaName, location, manager);
	}

	public void buttonAddNewFeatureClicked(String name, String description) {
		cinemaService.createFeature(name, description);
	}

	public void buttonSaveCinemaFeaturesButtonClicked(Cinema selectedCinema, Set<Feature> features) {
		cinemaService.editCinemaFeatures(selectedCinema, features);
	}

	public void buttonEditFeatureClicked(long id, String name, String description) {
		cinemaService.editFeature(id, name, description);

	}
}
