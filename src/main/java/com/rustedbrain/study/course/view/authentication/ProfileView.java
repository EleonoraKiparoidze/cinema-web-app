package com.rustedbrain.study.course.view.authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Panel;

public interface ProfileView extends ApplicationView {

	@Autowired
	void addListener(ViewListener listener);

	void showUserBlockWindow(long id, String login, UserRole userRole);

	void addAdministrationTab(User currUser, List<City> cities, List<Movie> movies, List<Manager> managers,
			Set<Feature> features, boolean isAdmin);

	void addStatisticsTab(List<City> cities);

	Panel createStatisticsTab(List<City> cities);

	void setAdminEditUserSelected(User currentUser);

	void addProfileEditTab(User currUser, List<City> cities);

	void addProfileAdminEditTab(User currUser, List<User> users, List<City> cities);

	void addMessageAdminTab(List<User> users, List<ChangeRequest> changeRequests);

	void addMessageTab(User currUser, List<ChangeRequest> changeRequests);

	void closeUserBlockWindow();

	interface ViewListener {

		void entered(ViewChangeListener.ViewChangeEvent event);

		void setView(ProfileView view);

		void buttonChangeNameClicked(long id, String newName);

		void buttonChangeLoginClicked(long id, String newLogin);

		void buttonChangeMailClicked(long id, String newMail);

		void buttonChangeSurnameClicked(long id, String surname);

		void buttonChangeCityClicked(long id, long cityId);

		void buttonChangeBirthdayClicked(long id, LocalDate birthday);

		void comboBoxEditUserSelected(User currentUser);

		void buttonUnblockClicked(long id);

		void buttonBlockClicked(long id);

		void buttonEditShowMeClicked(List<User> users);

		void buttonBlockSubmitClicked(long id, LocalDateTime blockDateTime, String blockDescription);

		CityEditPresenter getCityEditPresenter();

		CinemaEditPresenter getCinemaEditPresenter();

		CinemaHallEditPresenter getCinemaHallEditPresenter();

		void reload();

		MovieEditPresenter getMovieEditPresenter();

		FilmScreeningEditPresenter getFilmScreeningEditPresenter();

		void buttonFilmScreeningEventsClicked(FilmScreening selectedFilmScreening);

		void buttonFeaturesClicked(Cinema selectedCinema, Set<Feature> features);

		void buttonAcceptChangeRequestClicked(long userId, String fieldName, String value, long changeRequestId);

		void buttonDeclineChangeRequestClicked(long userId, String fieldName, String value, long changeRequestId);
	}

	void showFilmScreeningEventsWindow(FilmScreening selectedFilmScreening);

	void closeFilmScreeningEventsWindow();

	void closeFeaturesWindow();

	void showFeaturesWindow(Cinema selectedCinema, Set<Feature> features);

}