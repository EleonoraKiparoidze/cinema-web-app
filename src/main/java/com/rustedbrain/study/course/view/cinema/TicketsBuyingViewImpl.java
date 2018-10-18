package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.view.VaadinUI;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringView(name = VaadinUI.TICKET_BUYING_VIEW)
public class TicketsBuyingViewImpl extends VerticalLayout implements TicketsBuyingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4796514023161885274L;

	private List<TicketsBuyingView.TicketBuyViewListener> viewListeners = new ArrayList<>();

	private Panel ticketPanel;
	private Panel ticketInfoPanel;

	public TicketsBuyingViewImpl() {
		ticketPanel = getTicketPanel();
		ticketPanel.setContent(new VerticalLayout(getTicketInfoPanel()));
		addComponent(ticketPanel);
		setComponentAlignment(ticketPanel, Alignment.MIDDLE_CENTER);
	}

	private Panel getTicketInfoPanel() {
		if (ticketInfoPanel == null) {
			ticketInfoPanel = new Panel();
			ticketInfoPanel.setSizeUndefined();
		}
		return ticketInfoPanel;
	}

	private Panel getTicketPanel() {
		if (ticketPanel == null) {
			ticketPanel = new Panel();
			ticketPanel.setSizeFull();
		}
		return ticketPanel;
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		viewListeners.forEach(listener -> listener.entered(event));
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
	@Autowired
	public void addListener(TicketsBuyingView.TicketBuyViewListener ticketBuyViewListener) {
		ticketBuyViewListener.setView(this);
		this.viewListeners.add(ticketBuyViewListener);
	}

	@Override
	public void showFilmScreeningEvent(FilmScreeningEvent event, List<Seat> seats, String userName, String userSurname,
			String mail) {
		VerticalLayout ticketInfoPanelLayout = new VerticalLayout();

		FilmScreening filmScreening = event.getFilmScreening();
		Movie movie = filmScreening.getMovie();

		Label filmNameLabel = new Label(movie.getLocalizedName() + " (" + movie.getOriginalName() + ")");
		ticketInfoPanelLayout.addComponent(filmNameLabel);

		Label filmStartTimeLabel = new Label("Session start time: " + event.getTime());
		ticketInfoPanelLayout.addComponent(filmStartTimeLabel);

		Label cinemaHallNumberLabel = new Label("Cinema hall number: " + event.getCinemaHall().getName());
		ticketInfoPanelLayout.addComponent(cinemaHallNumberLabel);

		StringBuilder seatsStringBuilder = new StringBuilder();
		for (Seat seat : seats) {
			seatsStringBuilder.append("Seat ").append(seat.getNumber()).append(", row ")
					.append(seat.getRow().getNumber()).append(", price: ").append(seat.getPrice()).append(";\n");
		}
		seatsStringBuilder.append("\nTotal: ").append(seats.stream().mapToDouble(Seat::getPrice).sum());

		Label seatsLabel = new Label(seatsStringBuilder.toString(), ContentMode.PREFORMATTED);
		ticketInfoPanelLayout.addComponent(seatsLabel);

		TextField nameTextField = new TextField("Name", (userName == null ? "" : userName));
		ticketInfoPanelLayout.addComponent(nameTextField);
		TextField surnameTextField = new TextField("Surname", (userSurname == null ? "" : userSurname));
		ticketInfoPanelLayout.addComponent(surnameTextField);
		TextField mailTextField = new TextField("Mail", (mail == null ? "" : mail));
		ticketInfoPanelLayout.addComponent(mailTextField);

		HorizontalLayout buttonsHorizontalLayout = new HorizontalLayout();
		buttonsHorizontalLayout.addComponent(new Button("Reserve",
				(Button.ClickListener) event1 -> viewListeners
						.forEach(listener -> listener.buttonReserveClicked(nameTextField.getValue(),
								surnameTextField.getValue(), mailTextField.getValue()))));
		buttonsHorizontalLayout.addComponent(new Button("Buy",
				(Button.ClickListener) event1 -> viewListeners
						.forEach(listener -> listener.buttonBuyClicked(nameTextField.getValue(),
								surnameTextField.getValue(), mailTextField.getValue()))));

		ticketInfoPanelLayout.addComponent(buttonsHorizontalLayout);

		ticketInfoPanel.setContent(ticketInfoPanelLayout);
	}

	@Override
	public void showSuccessReserveMessage(String message) {
		Notification.show(message, Notification.Type.ASSISTIVE_NOTIFICATION);
	}
}
