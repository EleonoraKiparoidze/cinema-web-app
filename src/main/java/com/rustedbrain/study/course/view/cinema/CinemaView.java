package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Set;

public interface CinemaView extends ApplicationView {

	@Autowired
	void addCinemaViewListener(CinemaViewListener listener);

	void fillCinemaPanel(Cinema cinema, UserRole role, int availableToOrderDays, int currentDay);

	void setFilmScreenings(Set<FilmScreening> filmScreenings);

	void fillMenuPanel(AuthenticationService authenticationService);

	void setSelectedDay(LocalDate day);

	interface CinemaViewListener {

		void entered(ViewChangeListener.ViewChangeEvent event);

		void setView(CinemaView cinemaView);

		void buttonFilmViewTimeClicked(long id);

		void buttonDeleteCinemaClicked(long id);

		void buttonDayClicked(LocalDate day);

		void buttonRenameClicked(String value);

		void buttonShowMovieClicked(long id);
	}
}
