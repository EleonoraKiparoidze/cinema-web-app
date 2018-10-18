package com.rustedbrain.study.course.presenter.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.CityCinemasView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Logger;

@UIScope
@SpringComponent
public class CityCinemasViewPresenter implements CityCinemasView.CityCinemasViewListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 235588100969571636L;

	private static final Logger logger = Logger.getLogger(CityCinemasViewPresenter.class.getName());

	private static final int START_CINEMAS_PAGE = 1;
	private static final int CINEMAS_PER_PAGE = 10;

	private CityCinemasView view;
	private CinemaService cinemaService;

	private int currentCinemasPageNumber = START_CINEMAS_PAGE;
	private int currentCinemasPerPageCount = CINEMAS_PER_PAGE;

	@Autowired
	public CityCinemasViewPresenter(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}

	public void setView(CityCinemasView view) {
		this.view = view;
	}

	@Override
	public void buttonDeleteCinemaClicked(Long id) {
		cinemaService.deleteCinema(id);
		reloadCinemas();
	}

	@Override
	public void buttonCinemasPerPageCountClicked(int cinemasPerPageCount) {
		currentCinemasPerPageCount = cinemasPerPageCount;
		reloadCinemas();
	}

	@Override
	public void buttonPageClicked(int page) {
		currentCinemasPageNumber = page;
		reloadCinemas();
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		String param = event.getParameters();
		Optional<String> optionalCityName = Optional.ofNullable(param);
		if (optionalCityName.isPresent()) {
			Long cityId = Long.parseLong(optionalCityName.get());
			view.showCinemasPanel(cinemaService.getCityCinemas(cityId));
		} else {
			logger.warning("City was not selected, navigating to cities view...");
			new PageNavigator().navigateToCitiesView();
		}
	}

	private void reloadCinemas() {
		Page<Cinema> cinemaPage = cinemaService.getCinemasPage(currentCinemasPageNumber, currentCinemasPerPageCount);
		view.setCinemasPageCount(cinemaPage.getTotalPages());
		view.setCurrentCinemasPage(currentCinemasPageNumber);
		view.showCinemasPanel(cinemaPage.getContent());
	}
}
