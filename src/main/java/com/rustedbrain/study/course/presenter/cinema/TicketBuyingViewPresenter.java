package com.rustedbrain.study.course.presenter.cinema;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.rustedbrain.study.course.model.dto.TicketInfo;
import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.authorization.Member;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.TicketsBuyingView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class TicketBuyingViewPresenter implements TicketsBuyingView.TicketBuyViewListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6587607996897491417L;
	public static final String EVENT_ID_PARAM_KEY = "event_id";
	public static final String SEATS_ID_PARAM_KEY = "seats_id";
	public static final String PARAM_SEPARATOR = ", ";
	private static final Logger logger = Logger.getLogger(TicketBuyingViewPresenter.class.getName());
	private final CinemaService cinemaService;
	private final AuthenticationService authenticationService;
	private TicketsBuyingView view;

	private FilmScreeningEvent filmScreeningEvent;
	private List<Seat> seats;

	@Autowired
	public TicketBuyingViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
		this.authenticationService = authenticationService;
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		Map<String, String> parameterMap = event.getParameterMap();
		long eventId = Long.parseLong(parameterMap.get(EVENT_ID_PARAM_KEY));
		List<Long> seatIds = Arrays.stream(parameterMap.get(SEATS_ID_PARAM_KEY).split(PARAM_SEPARATOR))
				.map(String::trim).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
		this.filmScreeningEvent = cinemaService.getFilmScreeningEvent(eventId);
		this.seats = cinemaService.getSeats(seatIds);
		if ( authenticationService.isAuthenticated() && UserRole.MEMBER.equals(authenticationService.getUserRole()) ) {
			Member member = cinemaService.getMemberByLogin(authenticationService.getUserLogin());
			this.view.showFilmScreeningEvent(filmScreeningEvent, seats, member.getName(), member.getSurname(),
					member.getEmail());
		} else {
			this.view.showFilmScreeningEvent(filmScreeningEvent, seats, "", "", "");
		}
	}

	@Override
	public void setView(TicketsBuyingView view) {
		this.view = view;
	}

	@Override
	public void buttonReserveClicked(String name, String surname, String value) {
		if ( name == null || name.isEmpty() ) {
			view.showError("Please specify your name");
		} else if ( surname == null || surname.isEmpty() ) {
			view.showError("Please specify your surname");
		} else {
			try {
				if ( authenticationService.isAuthenticated() ) {
					List<TicketInfo> tickets = cinemaService.reserveTickets(name, surname,
							authenticationService.getUserLogin(), filmScreeningEvent, seats);
					logger.info("TicketInfo's successfully reserved: " + tickets);
					new PageNavigator()
							.navigateToTicketsInfoView(tickets.stream().mapToLong(TicketInfo::getId).toArray());
				} else {
					List<TicketInfo> tickets = cinemaService.reserveTickets(name, surname, filmScreeningEvent, seats);
					logger.info("TicketInfo's successfully reserved: " + tickets);
					new PageNavigator()
							.navigateToTicketsInfoView(tickets.stream().mapToLong(TicketInfo::getId).toArray());
				}
			} catch (IllegalArgumentException ex) {
				view.showError(ex.getMessage());
			} catch (DataIntegrityViolationException ex) {
				view.showError("Seems tickets already bought, please check your email");
			}
		}
	}

	@Override
	public void buttonBuyClicked(String name, String surname, String value) {
		if ( name == null || name.isEmpty() ) {
			view.showError("Please specify your name");
		} else if ( surname == null || surname.isEmpty() ) {
			view.showError("Please specify your surname");
		} else {
			try {
				if ( authenticationService.isAuthenticated() ) {
					List<TicketInfo> tickets = cinemaService.reserveTickets(name, surname,
							authenticationService.getUserLogin(), filmScreeningEvent, seats);
					logger.info("TicketInfo's successfully reserved: " + tickets);
					new PageNavigator()
							.navigateToTicketsLiqPay(tickets.stream().mapToLong(TicketInfo::getId).toArray());
				} else {
					List<TicketInfo> tickets = cinemaService.reserveTickets(name, surname, filmScreeningEvent, seats);
					logger.info("TicketInfo's successfully reserved: " + tickets);
					new PageNavigator()
							.navigateToTicketsLiqPay(tickets.stream().mapToLong(TicketInfo::getId).toArray());
				}
			} catch (IllegalArgumentException ex) {
				view.showError(ex.getMessage());
			} catch (DataIntegrityViolationException ex) {
				view.showError("Seems tickets already bought, please check your email");
			}
		}
	}
}
