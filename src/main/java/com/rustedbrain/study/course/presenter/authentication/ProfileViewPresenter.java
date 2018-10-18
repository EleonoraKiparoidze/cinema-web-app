package com.rustedbrain.study.course.presenter.authentication;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.dto.AuthUser;
import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.authorization.ChangeRequest;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Feature;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.presenter.authentication.util.CinemaEditPresenter;
import com.rustedbrain.study.course.presenter.authentication.util.CinemaHallEditPresenter;
import com.rustedbrain.study.course.presenter.authentication.util.CityEditPresenter;
import com.rustedbrain.study.course.presenter.authentication.util.FilmScreeningEditPresenter;
import com.rustedbrain.study.course.presenter.authentication.util.MovieEditPresenter;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class ProfileViewPresenter implements Serializable, ProfileView.ViewListener {

	private static final long serialVersionUID = -5933356439072774517L;
	private static final Logger logger = Logger.getLogger(ProfileViewPresenter.class.getName());
	private final CinemaService cinemaService;
	private final AuthenticationService authenticationService;
	private final CityEditPresenter cityEditPresenter;

	private ProfileView view;
	private CinemaEditPresenter cinemaEditPresenter;
	private CinemaHallEditPresenter cinemaHallEditPresenter;
	private MovieEditPresenter movieEditPresenter;
	private FilmScreeningEditPresenter filmScreeningEditPresenter;

	@Autowired
	public ProfileViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
		this.authenticationService = authenticationService;
		cityEditPresenter = new CityEditPresenter(cinemaService);
		cinemaEditPresenter = new CinemaEditPresenter(cinemaService);
		cinemaHallEditPresenter = new CinemaHallEditPresenter(cinemaService);
		movieEditPresenter = new MovieEditPresenter(cinemaService);
		filmScreeningEditPresenter = new FilmScreeningEditPresenter(cinemaService);
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		UserRole role = authenticationService.getUserRole();
		switch (role) {
		case ADMINISTRATOR:
		case MANAGER: {
			addMessagesTab(role);
			addProfileEditTab(role);
			addAdministrationTab(role);
			addStatisticsTab(role);
		}
			break;
		case PAYMASTER:
		case MODERATOR: {
			addMessagesTab(role);
			addProfileEditTab(role);
			addStatisticsTab(role);
		}
			break;
		case MEMBER: {
			addMessagesTab(role);
			addProfileEditTab(role);
		}
			break;
		case NOT_AUTHORIZED:
			view.showError("User not authorized.");
			break;
		}
	}

	private void addStatisticsTab(UserRole role) {
		List<City> cities = cinemaService.getCities();
		view.addStatisticsTab(cities);
	}

	private void addAdministrationTab(UserRole role) {
		User currUser = authenticationService.getAuthenticUser();
		List<Manager> managers = authenticationService.getManagers();
		List<City> cities = cinemaService.getCities();
		List<Movie> movies = cinemaService.getMovies();
		List<Feature> features = cinemaService.getFeatures();
		switch (role) {
		case ADMINISTRATOR: {
			view.addAdministrationTab(currUser, cities, movies, managers, new HashSet<>(features), true);
		}
			break;
		case MANAGER: {
			Optional<Manager> optionalManager =
					managers.stream().filter(userPredicate -> userPredicate.equals(currUser)).findFirst();
			if ( optionalManager.isPresent() ) {
				List<Cinema> cinemas = new ArrayList<>();
				cities.forEach(city -> cinemas.addAll(city.getCinemas()));
				List<Cinema> managerCinemas = cinemas.stream()
						.filter(cinemaPredicate -> cinemaPredicate.getManager() != null
								&& cinemaPredicate.getManager().equals(optionalManager.get()))
						.collect(Collectors.toList());
				if ( !managerCinemas.isEmpty() ) {
					List<City> managerCities = new ArrayList<>();
					managerCinemas.forEach(cinema -> managerCities.add(cinema.getCity()));
					view.addAdministrationTab(currUser, managerCities, movies,
							Collections.singletonList(optionalManager.get()), new HashSet<>(features), false);
				}
			}
		}
			break;
		default: {
			view.showError("You Don't Have Permission to Access ");
		}
		}
	}

	private void addProfileEditTab(UserRole role) {
		User currUser = authenticationService.getAuthenticUser();
		List<City> cities = cinemaService.getCities();

		switch (role) {
		case ADMINISTRATOR: {
			List<User> users = authenticationService.getUsersByRoles(Arrays.stream(UserRole.values())
					.filter(rolePredicate -> !rolePredicate.equals(UserRole.NOT_AUTHORIZED))
					.collect(Collectors.toList()));
			view.addProfileAdminEditTab(currUser, users, cities);
		}
			break;
		case MODERATOR: {
			List<User> users = authenticationService.getUsersByRoles(Collections.singletonList(UserRole.MEMBER));
			users.add(currUser);
			view.addProfileAdminEditTab(currUser, users, cities);
		}
			break;
		default: {
			view.addProfileEditTab(currUser, cities);
		}
		}
	}

	private void addMessagesTab(UserRole role) {
		User currUser = authenticationService.getAuthenticUser();
		List<ChangeRequest> changeRequests = authenticationService.getChangeRequests();
		switch (role) {
		case ADMINISTRATOR: {
			List<User> users = authenticationService.getUsersByRoles(Arrays.stream(UserRole.values())
					.filter(rolePredicate -> !rolePredicate.equals(UserRole.NOT_AUTHORIZED))
					.collect(Collectors.toList()));
			view.addMessageAdminTab(users, changeRequests);
		}
			break;
		default: {
			view.addMessageTab(currUser, changeRequests);
		}
		}
	}

	@Override
	public void setView(ProfileView view) {
		this.view = view;
	}

	@Override
	public void buttonChangeNameClicked(long id, String name) {
		authenticationService.changeUserName(id, name, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonChangeLoginClicked(long id, String login) {
		authenticationService.changeUserLogin(id, login, authenticationService.getUserRole());
		if ( authenticationService.getUserRole().equals(UserRole.ADMINISTRATOR) ) {
			authenticationService.logOut();
		} else {
			view.reload();
		}
	}

	@Override
	public void buttonChangeMailClicked(long id, String mail) {
		authenticationService.changeUserMail(id, mail, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonChangeSurnameClicked(long id, String surname) {
		authenticationService.changeUserSurname(id, surname, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonChangeCityClicked(long id, long cityId) {
		authenticationService.changeUserCity(id, cityId, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonChangeBirthdayClicked(long id, LocalDate birthday) {
		authenticationService.changeUserBirthday(id, birthday, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void comboBoxEditUserSelected(User selectedUser) {
		view.setAdminEditUserSelected(selectedUser);
	}

	@Override
	public void buttonUnblockClicked(long id) {
		authenticationService.unblockUser(id, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonBlockClicked(long id) {
		Optional<AuthUser> user = authenticationService.getAuthUserById(id);
		user.ifPresent(authUser -> {
			logger.info("User successfully retrieved: " + authUser);
			view.showUserBlockWindow(id, authUser.getLogin(), authUser.getUserRole());
		});
	}

	@Override
	public void buttonEditShowMeClicked(List<User> users) {
		String myLogin = authenticationService.getUserLogin();
		Optional<User> userOptional = users.stream().filter(user -> user.getLogin().equals(myLogin)).findAny();
		userOptional.ifPresent(user -> view.setAdminEditUserSelected(user));
	}

	@Override
	public void buttonBlockSubmitClicked(long id, LocalDateTime blockDateTime, String blockDescription) {
		authenticationService.changeUserBlockUntilDate(id, blockDateTime, blockDescription,
				authenticationService.getUserRole());
		view.closeUserBlockWindow();
		view.showWarning("User successfully blocked until " + blockDateTime);
	}

	@Override
	public CityEditPresenter getCityEditPresenter() {
		return cityEditPresenter;
	}

	@Override
	public CinemaEditPresenter getCinemaEditPresenter() {
		return cinemaEditPresenter;
	}

	@Override
	public CinemaHallEditPresenter getCinemaHallEditPresenter() {
		return cinemaHallEditPresenter;
	}

	@Override
	public void reload() {
		view.reload();

	}

	@Override
	public MovieEditPresenter getMovieEditPresenter() {
		return movieEditPresenter;
	}

	@Override
	public FilmScreeningEditPresenter getFilmScreeningEditPresenter() {
		return filmScreeningEditPresenter;
	}

	@Override
	public void buttonFilmScreeningEventsClicked(FilmScreening selectedFilmScreening) {
		view.showFilmScreeningEventsWindow(selectedFilmScreening);
	}

	@Override
	public void buttonFeaturesClicked(Cinema selectedCinema, Set<Feature> features) {
		view.showFeaturesWindow(selectedCinema, features);
	}

	@Override
	public void buttonAcceptChangeRequestClicked(long userId, String fieldName, String value, long changeRequestId) {
		authenticationService.acceptChangeRequest(userId, fieldName, value, changeRequestId);
		view.reload();
	}

	@Override
	public void buttonDeclineChangeRequestClicked(long userId, String fieldName, String value, long changeRequestId) {
		authenticationService.declineChangeRequest(userId, fieldName, value, changeRequestId);
		view.reload();
	}
}
