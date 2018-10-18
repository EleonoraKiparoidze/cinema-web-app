package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;

import java.util.List;

public interface CityCinemasView extends ApplicationView {

	void showCinemasPanel(List<Cinema> cinemas);

	void addCityCinemasViewListener(CityCinemasViewListener listener);

	void setCinemasPageCount(int totalPages);

	void setCurrentCinemasPage(int currentCinemasPage);

	interface CityCinemasViewListener {

		void buttonDeleteCinemaClicked(Long id);

		void buttonCinemasPerPageCountClicked(int cinemasPerPageCount);

		void buttonPageClicked(int page);

		void entered(ViewChangeListener.ViewChangeEvent event);

		void setView(CityCinemasView cityCinemasView);
	}
}
