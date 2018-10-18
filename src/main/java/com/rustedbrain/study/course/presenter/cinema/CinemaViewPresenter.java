package com.rustedbrain.study.course.presenter.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.CinemaView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@UIScope
@SpringComponent
public class CinemaViewPresenter implements Serializable, CinemaView.CinemaViewListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3937517145454700484L;
	private static final Logger logger = Logger.getLogger(CinemaViewPresenter.class.getName());
	private static final int AVAILABLE_TO_ORDER_DAYS = 7;
	private final CinemaService cinemaService;
	private final AuthenticationService authenticationService;
	private int selectedToOrderDay = 0;
	private CinemaView cinemaView;
	private Cinema cinema;

	@Autowired
	public CinemaViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
		this.authenticationService = authenticationService;
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		cinemaView.fillMenuPanel(authenticationService);
		Optional<String> optionalCinemaId = Optional.ofNullable(event.getParameters());
		if (optionalCinemaId.isPresent()) {
			logger.info("Cinema id parameter available. Retrieving cinema by id...");
			Long cinemaId = Long.parseLong(optionalCinemaId.get());
			cinema = cinemaService.getCinema(cinemaId);
			cinemaView.fillCinemaPanel(cinema, authenticationService.getUserRole(), AVAILABLE_TO_ORDER_DAYS,
					selectedToOrderDay);
			cinemaView.setFilmScreenings(
					cinemaService.getDayFilmScreenings(cinema.getId(), LocalDate.now().plusDays(selectedToOrderDay)));
			cinemaView.setSelectedDay(LocalDate.now().plusDays(selectedToOrderDay));
		} else {
			logger.warning("Cinema id not available. Navigating to cinemas view...");
		}
	}

	@Override
	public void setView(CinemaView cinemaView) {
		this.cinemaView = cinemaView;
	}

	@Override
	public void buttonFilmViewTimeClicked(long id) {
		new PageNavigator().navigateToCinemaHallView(id);
	}

	@Override
	public void buttonDeleteCinemaClicked(long id) {
		long cityId = cinema.getCity().getId();
		cinemaService.deleteCinema(id);
		new PageNavigator().navigateToCityCinemasView(cityId);
	}

	@Override
	public void buttonDayClicked(LocalDate day) {
		Set<FilmScreening> filmScreenings = cinemaService.getDayFilmScreenings(cinema.getId(), day);
		cinemaView.setFilmScreenings(filmScreenings);
		cinemaView.setSelectedDay(day);
	}

	@Override
	public void buttonRenameClicked(String value) {
		cinemaService.renameCinema(cinema.getId(), value);
		cinemaView.reload();
	}

	@Override
	public void buttonShowMovieClicked(long id) {
		new PageNavigator().navigateToMovieView(id);
	}
}
