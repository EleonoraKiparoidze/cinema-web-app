package com.rustedbrain.study.course.view.authentication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.persistence.cinema.Actor;
import com.rustedbrain.study.course.model.persistence.cinema.Genre;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public interface MovieAdvancedEditView extends View {

	@Autowired
	void addListener(ViewListener viewListener);

	interface ViewListener {

		void setMovieEditView(MovieAdvancedEditView movieEditView);

		void entered(ViewChangeEvent event);

		void buttonSaveMovieButtonClicked(Movie editedMovie);

		void buttonAddNewGenreClicked(String name);

		void buttonAddNewActorClicked(String name, String surname);

		void buttonEditActorClicked(long id, String name, String surname);

		void buttonEditGenreClicked(long id, String name);
	}

	void showWarning(String message);

	void showError(String message);

	void reload();

	void setMovieInformation(Movie movie, List<Actor> actors, List<Genre> genres);
}