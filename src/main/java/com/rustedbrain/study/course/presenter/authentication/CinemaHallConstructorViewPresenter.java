package com.rustedbrain.study.course.presenter.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.exception.ResourceException;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.authentication.CinemaHallConstructorView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class CinemaHallConstructorViewPresenter implements CinemaHallConstructorView.ViewListener {
	private final AuthenticationService authenticationService;
	private final CinemaService cinemaService;
	private CinemaHallConstructorView cinemaHallConstructorView;
	private Map<Integer, List<Integer>> cinemaHallSeatCoordinateMap;
	private CinemaHall cinemaHall;

	private static final Logger logger = Logger.getLogger(CinemaHallConstructorViewPresenter.class.getName());

	@Autowired
	public CinemaHallConstructorViewPresenter(AuthenticationService authenticationService,
			CinemaService cinemaService) {
		this.authenticationService = authenticationService;
		this.cinemaService = cinemaService;
	}

	@Override
	public void setCinemaHallConstructorView(CinemaHallConstructorView cinemaHallConstructorView) {
		this.cinemaHallConstructorView = cinemaHallConstructorView;
	}

	@Override
	public void addRowButtonClicked(long cinameHallId, String row, String seats, String startCoordinate) {
		if ( isValidRowsSeats(row, seats, startCoordinate) ) {
			int rowNumber = Integer.parseInt(row) - 1;
			List<Integer> seatsList = new LinkedList<>();
			cinemaHallSeatCoordinateMap.remove(rowNumber);
			int start = Integer.parseInt(startCoordinate);
			for (int i = start; i < Integer.parseInt(seats); i++) {
				seatsList.add(i);
			}
			cinemaHallSeatCoordinateMap.put(rowNumber, seatsList);
			try {
				cinemaService.setCinemaHallSeatMap(cinameHallId, cinemaHallSeatCoordinateMap);
				cinemaHallConstructorView.reload();
			} catch (ParserConfigurationException | ResourceException | SAXException | IOException e) {
				logger.log(Level.SEVERE, e.getCause().toString());
			}
		}
	}

	private boolean isValidRowsSeats(String row, String seats, String startCoordinate) {
		boolean valid = true;
		try {
			int rowsInt = Integer.parseInt(row);
			if ( rowsInt <= 0 ) {
				valid = false;
				cinemaHallConstructorView.showError("Rows number must be under 0.");
			}
		} catch (NumberFormatException e) {
			valid = false;
			cinemaHallConstructorView.showError("Rows must be number.");
		}
		try {
			int seatsInt = Integer.parseInt(seats);
			if ( seatsInt <= 0 ) {
				valid = false;
				cinemaHallConstructorView.showError("Seats number must be under 0.");
			}
		} catch (NumberFormatException e) {
			valid = false;
			cinemaHallConstructorView.showError("Seats must be number.");
		}
		try {
			int startCoordinateInt = Integer.parseInt(startCoordinate);
			if ( startCoordinateInt < 0 ) {
				valid = false;
				cinemaHallConstructorView.showError("Start coordinate number must be under/equal 0.");
			}
		} catch (NumberFormatException e) {
			valid = false;
			cinemaHallConstructorView.showError("Start coordinate must be number.");
		}
		return valid;
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		Optional<String> optionalId = Optional.ofNullable(event.getParameters());
		if ( optionalId.isPresent() ) {
			Optional<CinemaHall> optionalCinemaHall = cinemaService.getCinemaHall(Long.parseLong(optionalId.get()));
			if ( optionalCinemaHall.isPresent() ) {
				this.cinemaHall = optionalCinemaHall.get();
				UserRole role = authenticationService.getUserRole();

				switch (role) {
				case ADMINISTRATOR: {
					addCinemaHallConstructorMenuComponents(cinemaHall);
					setCinemaHallSeatCoordinateMap(cinemaHall);
				}
					break;
				case PAYMASTER:
					break;
				case MANAGER:
					break;
				case MEMBER:
					break;
				case MODERATOR: {
				}
					break;
				case NOT_AUTHORIZED:
					cinemaHallConstructorView.showError("User not authorized.");
					break;
				}
			} else {
				this.cinemaHallConstructorView.showError("Cinema Hall with specified id not exist.");
			}
		} else {
			this.cinemaHallConstructorView.showError("Cinema Hall id not presented.");
		}
	}

	private void addCinemaHallConstructorMenuComponents(CinemaHall cinemaHall) {
		cinemaHallConstructorView.addCinemaHallConstructorMenuComponents(cinemaHall);
	}

	private void setCinemaHallSeatCoordinateMap(CinemaHall cinemaHall) {
		try {
			this.cinemaHallSeatCoordinateMap = cinemaService.getCinemaHallSeatCoordinateMap(cinemaHall);
			cinemaHallConstructorView.setCinemaHallSeatMap(cinemaHallSeatCoordinateMap);
		} catch (ParserConfigurationException | IOException | SAXException | ResourceException e) {
			logger.log(Level.WARNING, "Error occurred during retrieving seat map for cienma hall constructor view", e);
			cinemaHallConstructorView.showError(e.getMessage());
		}
	}

	@Override
	public void buttonSaveCinemaHallSeatsButtonClicked() {
		cinemaService.saveCinemaHallSeatsFromXML(cinemaHall.getId(), cinemaHallSeatCoordinateMap);
		new PageNavigator().navigateToProfileView();
	}

	@Override
	public void deleteRowButtonClicked(long id, String rowNumber) {
		try {
			int rowNumberInt = Integer.parseInt(rowNumber) - 1;
			if ( rowNumberInt >= 0 && cinemaHallSeatCoordinateMap.containsKey(rowNumberInt) ) {
				Map<Integer, List<Integer>> newCinemaHallSeatCoordinateMap = new HashMap<>();
				cinemaHallSeatCoordinateMap.remove(rowNumberInt);
				cinemaHallSeatCoordinateMap.entrySet().forEach(entry -> {
					if ( entry.getKey() < rowNumberInt ) {
						newCinemaHallSeatCoordinateMap.put(entry.getKey(), entry.getValue());
					} else if ( entry.getKey() > rowNumberInt ) {
						newCinemaHallSeatCoordinateMap.put(entry.getKey() - 1, entry.getValue());
					}
				});
				try {
					cinemaService.setCinemaHallSeatMap(id, newCinemaHallSeatCoordinateMap);
					cinemaHallConstructorView.reload();
				} catch (ParserConfigurationException | ResourceException | SAXException | IOException e) {
					logger.log(Level.SEVERE, e.getCause().toString());
				}
			} else {
				cinemaHallConstructorView.showError("Row number must exist and be under 0");
			}
		} catch (NumberFormatException e) {
			cinemaHallConstructorView.showError("Row number must be integer value");
		}
	}
}
