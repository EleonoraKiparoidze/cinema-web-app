package com.rustedbrain.study.course.view.cinema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.model.persistence.cinema.Row;
import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.model.persistence.cinema.Ticket;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = VaadinUI.CINEMA_HALL_VIEW)
public class CinemaHallViewImpl extends VerticalLayout implements CinemaHallView {

	private static final long serialVersionUID = -3307581243415244434L;
	private final AuthenticationService authenticationService;

	private List<CinemaHallView.CinemaHallViewListener> cinemaHallViewListeners = new ArrayList<>();

	private CinemaHallLayout cinemaHallLayout;
	private TicketBuyLayout ticketBuyLayout;
	private Panel cinemaHallPanel;

	@Autowired
	public CinemaHallViewImpl(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
		addComponentsAndExpand(new Panel(new MenuComponent(authenticationService)));
		addComponentsAndExpand(getCinemaHallPanel());
	}

	private Panel getCinemaHallPanel() {
		if ( cinemaHallPanel == null ) {
			cinemaHallPanel = new Panel();
		}
		return cinemaHallPanel;
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		cinemaHallViewListeners.forEach(listener -> listener.entered(event));
	}

	@Override
	public void fillFilmScreeningEventPanel(FilmScreeningEvent filmScreeningEvent) {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponentsAndExpand(createMovieInfoPanel(filmScreeningEvent));

		HorizontalLayout horizontalLayout = new HorizontalLayout();

		this.cinemaHallLayout = new CinemaHallLayout(filmScreeningEvent);
		this.cinemaHallLayout.setSizeFull();
		Panel cinemaHallLayoutPanel = new Panel();
		cinemaHallLayoutPanel.setSizeFull();
		cinemaHallLayoutPanel.setContent(this.cinemaHallLayout);
		horizontalLayout.addComponentsAndExpand(cinemaHallLayoutPanel);

		this.ticketBuyLayout = new TicketBuyLayout(filmScreeningEvent);
		this.ticketBuyLayout.setSizeFull();
		Panel ticketBuyLayoutPanel = new Panel();
		ticketBuyLayoutPanel.setSizeFull();
		ticketBuyLayoutPanel.setContent(this.ticketBuyLayout);
		horizontalLayout.addComponentsAndExpand(ticketBuyLayoutPanel);

		verticalLayout.addComponentsAndExpand(horizontalLayout);
		cinemaHallPanel.setContent(verticalLayout);
	}

	private Component createMovieInfoPanel(FilmScreeningEvent filmScreeningEvent) {
		Movie movie = filmScreeningEvent.getFilmScreening().getMovie();
		VerticalLayout verticalLayout =
				new VerticalLayout(
						new Label(
								movie.getLocalizedName() + " " + "(" + movie.getOriginalName() + ")" + "</br> "
										+ filmScreeningEvent.getDate() + " " + filmScreeningEvent.getTime(),
								ContentMode.HTML));
		verticalLayout.setSpacing(true);
		return new Panel(verticalLayout);
	}

	@Override
	public void displaySelectedSeats(Set<Seat> seats) {
		ticketBuyLayout.displaySelectedSeats(seats);
	}

	@Override
	@Autowired
	public void addListener(CinemaHallViewListener cinemaHallViewListener) {
		cinemaHallViewListener.setView(this);
		this.cinemaHallViewListeners.add(cinemaHallViewListener);
	}

	@Override
	public void showWarning(String message) {
		Notification.show(message, Notification.Type.WARNING_MESSAGE);
	}

	@Override
	public void showError(String message) {
		Notification.show(message, Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void reload() {
		Page.getCurrent().reload();
	}

	@Override
	public void unsetSelectedSeat(Seat seat) {
		cinemaHallLayout.unsetSelectedSeat(seat);
	}

	@Override
	public void setSelectedSeat(Seat seat) {
		cinemaHallLayout.setSelectedSeat(seat);
	}

	private class TicketBuyLayout extends VerticalLayout {

		private static final long serialVersionUID = -6631468621446960167L;
		private Label selectedSeatsLabel;
		private Button buttonBuyTickets;

		TicketBuyLayout(FilmScreeningEvent filmScreeningEvent) {
			this.selectedSeatsLabel = new Label();
			this.selectedSeatsLabel.setContentMode(ContentMode.PREFORMATTED);
			this.addComponent(this.selectedSeatsLabel);
			this.buttonBuyTickets = new Button("Apply", (Button.ClickListener) event -> cinemaHallViewListeners
					.forEach(CinemaHallViewListener::buttonBuyTicketClicked));
			this.buttonBuyTickets.setEnabled(false);
			this.buttonBuyTickets.setSizeFull();
			this.addComponent(this.buttonBuyTickets);
		}

		void displaySelectedSeats(Set<Seat> seats) {
			if ( seats.isEmpty() ) {
				this.selectedSeatsLabel.setValue("No seats selected.");
				this.buttonBuyTickets.setEnabled(false);
			} else {
				StringBuilder builder = new StringBuilder();
				for (Seat seat : seats) {
					builder.append("Seat ").append(seat.getNumber()).append(", row ").append(seat.getRow().getNumber())
							.append(", price: ").append(seat.getPrice()).append(";\n");
				}
				builder.append("\nTotal: ").append(seats.stream().mapToDouble(Seat::getPrice).sum());
				this.selectedSeatsLabel.setValue(builder.toString());
				this.buttonBuyTickets.setEnabled(true);
			}
		}
	}

	private class CinemaHallLayout extends VerticalLayout {

		private static final long serialVersionUID = 6543704383454790019L;
		private List<SeatButton> seatButtonList;

		CinemaHallLayout(FilmScreeningEvent filmScreeningEvent) {
			CinemaHall cinemaHall = filmScreeningEvent.getCinemaHall();
			this.seatButtonList = new ArrayList<>();
			for (Row row : new TreeSet<>(cinemaHall.getRows())) {
				HorizontalLayout rowHorizontalLayout = new HorizontalLayout();
				rowHorizontalLayout.addComponent(new Label(Integer.toString(row.getNumber())));

				for (Seat seat : new TreeSet<>(row.getSeats())) {
					Button button = new Button(Integer.toString(seat.getNumber()));
					Optional<Ticket> optionalTicket = filmScreeningEvent.getTickets().stream()
							.filter(ticket -> ticket.getSeat().equals(seat)).findAny();
					if ( optionalTicket.isPresent() ) {
						button.setStyleName(ValoTheme.BUTTON_DANGER);
						if ( authenticationService.isCinemaManagementAvailable(cinemaHall.getCinema().getId()) ) {
							Ticket ticket = optionalTicket.get();
							button.setDescription("<ul>" + "<li>Name: " + ticket.getClientName() + "</li>"
									+ "<li>Surname: " + ticket.getClientSurname() + "</li>" + "<li>Sold: "
									+ (ticket.getSoldDate() != null ? ticket.getSoldDate().toString() : "Reserved")
									+ "</li>" + "</ul>", ContentMode.HTML);
						}
					} else {
						button.addClickListener((Button.ClickListener) event -> {
							cinemaHallViewListeners.forEach(listener -> listener.fireSeatSelected(seat));
						});
					}
					seatButtonList.add(new SeatButton(button, seat));
					rowHorizontalLayout.addComponent(button);
				}
				this.addComponent(rowHorizontalLayout);
			}
		}

		void unsetSelectedSeat(Seat seat) {
			Optional<SeatButton> seatButton =
					seatButtonList.stream().filter(button -> button.getSeat().equals(seat)).findAny();
			seatButton.ifPresent(button -> button.getButton().removeStyleName(ValoTheme.BUTTON_FRIENDLY));
		}

		void setSelectedSeat(Seat seat) {
			Optional<SeatButton> seatButton =
					seatButtonList.stream().filter(button -> button.getSeat().equals(seat)).findAny();
			seatButton.ifPresent(button -> button.getButton().setStyleName(ValoTheme.BUTTON_FRIENDLY));
		}

		private class SeatButton {

			private Button button;
			private Seat seat;

			SeatButton(Button button, Seat seat) {
				this.button = button;
				this.seat = seat;
			}

			public Button getButton() {
				return button;
			}

			public Seat getSeat() {
				return seat;
			}

			@Override
			public boolean equals(Object o) {
				if ( this == o )
					return true;
				if ( o == null || getClass() != o.getClass() )
					return false;

				SeatButton that = (SeatButton) o;

				return seat.equals(that.seat);
			}

			@Override
			public int hashCode() {
				return seat.hashCode();
			}
		}
	}
}
