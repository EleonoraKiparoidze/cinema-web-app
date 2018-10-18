package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface TicketsBuyingView extends ApplicationView {

	@Autowired
	void addListener(TicketBuyViewListener ticketBuyViewListener);

	void showFilmScreeningEvent(FilmScreeningEvent event, List<Seat> seatIds, String userName, String userSurname,
			String mail);

	void showSuccessReserveMessage(String message);

	interface TicketBuyViewListener {

		void entered(ViewChangeListener.ViewChangeEvent event);

		void setView(TicketsBuyingView ticketsBuyingView);

		void buttonReserveClicked(String name, String surname, String value);

		void buttonBuyClicked(String name, String surname, String value);
	}
}
