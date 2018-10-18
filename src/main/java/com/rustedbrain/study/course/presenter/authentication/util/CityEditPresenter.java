package com.rustedbrain.study.course.presenter.authentication.util;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.service.CinemaService;

public class CityEditPresenter {
	private final CinemaService cinemaService;

	public CityEditPresenter(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}

	public void buttonDeleteCityClicked(City selectedCity) {
		cinemaService.deleteCity(selectedCity.getName());
	}

	public void buttonSaveCityClicked(City selectedCity, String newCityName) {
		cinemaService.renameCity(selectedCity, newCityName);
	}

	public void buttonAddNewCityClicked(String newCityName) {
		cinemaService.createCity(newCityName);
	}
}
