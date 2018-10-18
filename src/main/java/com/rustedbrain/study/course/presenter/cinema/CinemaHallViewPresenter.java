package com.rustedbrain.study.course.presenter.cinema;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.CinemaHallView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class CinemaHallViewPresenter implements CinemaHallView.CinemaHallViewListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3731734121259034867L;
	private static final Logger logger = Logger.getLogger(CinemaHallViewPresenter.class.getName());
	private final CinemaService cinemaService;
	private CinemaHallView view;
	private FilmScreeningEvent filmScreeningEvent;
	private Set<Seat> selectedSeats;

	@Autowired
	public CinemaHallViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
	}

	@Override
	public void fireSeatSelected(Seat seat) {
		if ( selectedSeats.contains(seat) ) {
			this.selectedSeats.remove(seat);
			this.view.unsetSelectedSeat(seat);
			this.view.displaySelectedSeats(selectedSeats);
		} else {
			this.selectedSeats.add(seat);
			this.view.setSelectedSeat(seat);
			this.view.displaySelectedSeats(selectedSeats);
		}
	}

	@Override
	public void setView(CinemaHallView view) {
		this.view = view;
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		Optional<String> optionalId = Optional.ofNullable(event.getParameters());
		if ( optionalId.isPresent() ) {
			this.selectedSeats = new HashSet<>();
			this.filmScreeningEvent = cinemaService.getFilmScreeningEvent(Long.parseLong(optionalId.get()));
			this.view.fillFilmScreeningEventPanel(this.filmScreeningEvent);
		} else {
			logger.warning("Film screening event id is not presented. Navigating to previous view...");
			// TODO navigate to previous view, what to do with params?
		}
	}

	@Override
	public void buttonBuyTicketClicked() {
		if ( selectedSeats.isEmpty() ) {
			view.showWarning("Please select at least one seat to buy");
		} else {
			new PageNavigator().navigateToTicketBuyingView(filmScreeningEvent.getId(),
					selectedSeats.stream().mapToLong(Seat::getId).toArray());
		}
	}
}
