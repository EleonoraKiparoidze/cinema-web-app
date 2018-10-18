package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;

import java.util.Set;

public interface CinemaHallView extends ApplicationView {

	void displaySelectedSeats(Set<Seat> seats);

	void addListener(CinemaHallViewListener cinemaHallViewListener);

	void fillFilmScreeningEventPanel(FilmScreeningEvent filmScreeningEvent);

	void unsetSelectedSeat(Seat seat);

	void setSelectedSeat(Seat seat);

	interface CinemaHallViewListener {

		void fireSeatSelected(Seat seat);

		void setView(CinemaHallView cinemaHallView);

		void entered(ViewChangeListener.ViewChangeEvent event);

		void buttonBuyTicketClicked();
	}
}
