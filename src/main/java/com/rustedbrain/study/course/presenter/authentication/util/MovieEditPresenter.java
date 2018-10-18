package com.rustedbrain.study.course.presenter.authentication.util;

import java.time.LocalDateTime;

import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.util.PageNavigator;

public class MovieEditPresenter {
	private final CinemaService cinemaService;

	public MovieEditPresenter(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}

	public void buttonAdvancedEditMovieClicked(Movie selectedMovie) {
		new PageNavigator().navigateToMovieEditView(selectedMovie.getId());
	}

	public void buttonDeleteMovieClicked(long id) {
		cinemaService.deleteMovie(id);
	}

	public void buttonSaveMovieClicked(Movie selectedMovie, String newOriginalMovieName, String newLocalizeMovieName,
			String newCountry, LocalDateTime newMovieReleaseDate) {
		cinemaService.editMovie(selectedMovie, newOriginalMovieName, newLocalizeMovieName, newCountry,
				newMovieReleaseDate);

	}

	public void buttonAddNewMovieClicked(String localizedName, String originalName, LocalDateTime releaseDate) {
		long movieId = cinemaService.createMovie(localizedName, originalName, releaseDate);
		new PageNavigator().navigateToMovieEditView(movieId);
	}

}
