package com.rustedbrain.study.course.presenter.authentication;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.authentication.MovieAdvancedEditView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class MovieAdvancedEditViewPresenter implements MovieAdvancedEditView.ViewListener {

	private final AuthenticationService authenticationService;
	private final CinemaService cinemaService;
	private MovieAdvancedEditView movieEditView;
	private Movie movie;

	@Autowired
	public MovieAdvancedEditViewPresenter(AuthenticationService authenticationService, CinemaService cinemaService) {
		this.authenticationService = authenticationService;
		this.cinemaService = cinemaService;
	}

	@Override
	public void setMovieEditView(MovieAdvancedEditView movieEditView) {
		this.movieEditView = movieEditView;
	}

	@Override
	public void entered(ViewChangeEvent event) {
		Optional<String> optionalId = Optional.ofNullable(event.getParameters());
		if ( optionalId.isPresent() ) {
			Optional<Movie> optionalMovie = cinemaService.getMovie(Long.parseLong(optionalId.get()));
			if ( optionalMovie.isPresent() ) {
				this.movie = optionalMovie.get();
				UserRole role = authenticationService.getUserRole();

				switch (role) {
				case ADMINISTRATOR: {
					addMovieInformation(movie);
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
					this.movieEditView.showError("User not authorized.");
					break;
				}
			} else {
				this.movieEditView.showError("Cinema Hall with specified id not exist.");
			}
		} else {
			this.movieEditView.showError("Cinema Hall id not presented.");
		}

	}

	private void addMovieInformation(Movie movie) {
		this.movieEditView.setMovieInformation(movie, cinemaService.getActors(), cinemaService.getGenres());
	}

	@Override
	public void buttonSaveMovieButtonClicked(Movie editedMovie) {
		cinemaService.editMovie(editedMovie);
		new PageNavigator().navigateToProfileView();
	}

	@Override
	public void buttonAddNewGenreClicked(String name) {
		cinemaService.createGenre(name);
	}

	@Override
	public void buttonAddNewActorClicked(String name, String surname) {
		cinemaService.createActor(name, surname);
	}

	@Override
	public void buttonEditActorClicked(long id, String name, String surname) {
		cinemaService.editActor(id, name, surname);
	}

	@Override
	public void buttonEditGenreClicked(long id, String name) {
		cinemaService.editGenre(id, name);
	}

}
