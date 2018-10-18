package com.rustedbrain.study.course.presenter.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.MainView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@UIScope
@SpringComponent
public class MainViewPresenter implements MainView.MainViewListener, Serializable {

	private static final long serialVersionUID = -6234491830993571755L;

	private static final Logger logger = Logger.getLogger(MainViewPresenter.class.getName());

	private MainView mainView;
	private CinemaService cinemaService;
	private AuthenticationService authenticationService;

	@Autowired
	public MainViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
		this.authenticationService = authenticationService;
	}

	public void setView(MainView mainView) {
		this.mainView = mainView;
	}

	@Override
	public void cityButtonClicked(long cityId) {
		new PageNavigator().navigateToCityCinemasView(cityId);
	}

	@Override
	public void alphabetButtonClicked(Character character) {
		mainView.setSelectedCharacterCities(character);
	}

	@Override
	public void buttonDeleteCityClicked(String cityName) {
		try {
			cinemaService.deleteCity(cityName);
			mainView.reload();
		} catch (IllegalArgumentException ex) {
			mainView.showWarning(ex.getMessage());
		} catch (Exception ex) {
			mainView.showError(ex.getMessage());
		}
	}

	@Override
	public void buttonCreateCityClicked(String cityName) {
		try {
			cinemaService.createCity(cityName);
			mainView.reload();
		} catch (IllegalArgumentException ex) {
			mainView.showWarning(ex.getMessage());
		} catch (Exception ex) {
			mainView.showError(ex.getMessage());
		}
	}

	@Override
	public void buttonShowCinemaClicked(Cinema cinema) {
		new PageNavigator().navigateToCinemaView(cinema.getId());
	}

	@Override
	public void entered() {
		mainView.fillMenuPanel(authenticationService);
		try {
			InetAddress address = InetAddress.getByName(Page.getCurrent().getWebBrowser().getAddress());

			Optional<City> optionalCity = cinemaService.getCityByInetAddress(address);

			if ( optionalCity.isPresent() ) {
				City city = optionalCity.get();
				Optional<Cinema> optionalCinema = cinemaService.getNearestCinema(address, city);
				if ( optionalCinema.isPresent() ) {
					List<FilmScreening> filmScreenings =
							cinemaService.getTodayCinemaFilmScreenings(optionalCinema.get());
					mainView.fillFilmScreeningsPanel(filmScreenings);
				} else {
					new PageNavigator().navigateToCityCinemasView(city.getId());
				}
			} else {
				throw new NoSuchElementException("User city was not identified");
			}
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Error occured while binding " + MainViewPresenter.class.getSimpleName() + " to "
					+ MainView.class.getSimpleName(), ex);
		} catch (NoSuchElementException ex) {
			logger.log(Level.INFO, "User location was not identified", ex);
			List<City> cities = cinemaService.getCities();
			mainView.showCitySelectionPanel(cities);
			Optional<char[]> optional = cities.stream().sorted().map(City::getName).map(String::toCharArray).findAny();
			optional.ifPresent(chars -> mainView.setSelectedCharacterCities(chars[0]));
		}
	}

	@Override
	public void comboBoxCitySelectionValueSelected(City city) {
		new PageNavigator().navigateToCityCinemasView(city.getId());
	}
}
