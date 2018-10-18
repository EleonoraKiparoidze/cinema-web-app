package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.ApplicationView;

import java.util.List;

public interface CitiesView extends ApplicationView {

	void fillCitiesPanel(List<City> cities);

	void addCitiesViewListener(CitiesView.CitiesViewListener listener);

	void setCurrentCitiesPageNumber(int currentCitiesPageNumber, int totalPages);

	interface CitiesViewListener {

		void buttonDeleteCityClicked(Long id);

		void buttonCitiesPerPageCountClicked(int citiesPerPageCount);

		void buttonPageClicked(int page);

		void setView(CitiesView citiesView);
	}
}
