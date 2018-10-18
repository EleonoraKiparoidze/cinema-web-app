package com.rustedbrain.study.course.presenter.cinema;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.CitiesView;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class CitiesViewPresenter implements CitiesView.CitiesViewListener, Serializable {

	private static final long serialVersionUID = 6863586710948885079L;

	private static final int START_CINEMAS_PAGE = 1;
	private static final int CINEMAS_PER_PAGE = 10;

	private CitiesView view;
	private CinemaService cinemaService;

	private int currentCitiesPageNumber = START_CINEMAS_PAGE;
	private int currentCitiesPerPageCount = CINEMAS_PER_PAGE;

	@Autowired
	public CitiesViewPresenter(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}

	public void setView(CitiesView view) {
		this.view = view;
	}

	@Override
	public void buttonDeleteCityClicked(Long id) {
		cinemaService.deleteCity(id);
		reloadCities();
	}

	@Override
	public void buttonCitiesPerPageCountClicked(int citiesPerPageCount) {
		currentCitiesPerPageCount = citiesPerPageCount;
		reloadCities();
	}

	@Override
	public void buttonPageClicked(int page) {
		currentCitiesPageNumber = page;
		reloadCities();
	}

	private void reloadCities() {
		Page<City> cinemaPage = cinemaService.getCitiesPage(currentCitiesPageNumber, currentCitiesPerPageCount);
		view.setCurrentCitiesPageNumber(currentCitiesPageNumber, cinemaPage.getTotalPages());
		view.fillCitiesPanel(cinemaPage.getContent());
	}
}
