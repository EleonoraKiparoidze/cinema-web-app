package com.rustedbrain.study.course.presenter.cinema;

import com.rustedbrain.study.course.model.dto.AuthUser;
import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.model.persistence.cinema.Comment;
import com.rustedbrain.study.course.model.persistence.cinema.CommentReputation;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.MovieView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@UIScope
@SpringComponent
public class MovieViewPresenter implements MovieView.Listener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5390913228223487155L;
	private final AuthenticationService authenticationService;
	private final CinemaService cinemaService;
	private Movie movie;
	private MovieView view;

	@Autowired
	public MovieViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
		this.authenticationService = authenticationService;
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		Optional<String> optionalId = Optional.ofNullable(event.getParameters());
		if ( optionalId.isPresent() ) {
			Optional<Movie> optionalMovie = cinemaService.getMovie(Long.parseLong(optionalId.get()));
			if ( optionalMovie.isPresent() ) {
				this.movie = optionalMovie.get();
				boolean authenticated = authenticationService.isAuthenticated();
				String userLogin = authenticated ? authenticationService.getUserLogin() : null;
				this.view.showMovieInfo(movie, authenticated, userLogin,
						authenticationService.getUserRole().equals(UserRole.ADMINISTRATOR),
						authenticationService.getUserRole().equals(UserRole.MODERATOR));
			} else {
				this.view.showError("Movie with specified id not exist.");
			}
		} else {
			this.view.showError("Movie id not presented.");
		}
	}

	@Override
	public void setView(MovieView view) {
		this.view = view;
	}

	@Override
	public void buttonProfileClicked(long id) {
		new PageNavigator().navigateToProfileInfoView(id);
	}

	@Override
	public void buttonCreateMessageClicked(String textArea) {
		if ( authenticationService.isAuthenticated() ) {
			cinemaService.createMessage(movie, authenticationService.getAuthenticUser(), textArea);
			view.reload();
		} else {
			view.showError("Only registered user can create messages");
		}
	}

	@Override
	public void buttonDeleteCommentClicked(long id) {
		cinemaService.deleteComment(id);
		view.reload();
	}

	@Override
	public void buttonEditCommentClicked(long commentId, String newValue) {
		cinemaService.editComment(commentId, newValue);
	}

	@Override
	public void buttonPlusClicked(Comment comment) {
		User user = authenticationService.getAuthenticUser();
		try {
			Optional<CommentReputation> commentReputationOptional = comment.getCommentReputations().stream()
					.filter(commentReputation -> commentReputation.getUser().equals(user)).findAny();
			if ( !commentReputationOptional.isPresent() ) {
				cinemaService.addPlusCommentReputation(comment.getId(), user);
				view.reload();
			} else if ( !commentReputationOptional.get().isPlus() ) {
				cinemaService.invertCommentReputation(commentReputationOptional.get());
				view.reload();
			} else {
				view.showError("Cannot add reputation to comment twice");
			}
		} catch (Exception ex) {
			view.showError(ex.getMessage());
		}
	}

	@Override
	public void buttonMinusClicked(Comment comment) {
		User user = authenticationService.getAuthenticUser();
		try {
			Optional<CommentReputation> commentReputationOptional = comment.getCommentReputations().stream()
					.filter(commentReputation -> commentReputation.getUser().equals(user)).findAny();
			if ( !commentReputationOptional.isPresent() ) {
				cinemaService.addMinusCommentReputation(comment.getId(), user);
				view.reload();
			} else if ( commentReputationOptional.get().isPlus() ) {
				cinemaService.invertCommentReputation(commentReputationOptional.get());
				view.reload();
			} else {
				view.showError("Cannot minus reputation to comment twice");
			}
		} catch (Exception ex) {
			view.showError(ex.getMessage());
		}
	}

	@Override
	public void buttonBlockAndDeleteClicked(long commentId, long userId) {
		Optional<AuthUser> user = authenticationService.getAuthUserById(userId);
		user.ifPresent(authUser -> {
			view.showUserBlockDeleteWindow(userId, authUser.getLogin(), authUser.getUserRole(), commentId);
		});
	}

	@Override
	public void buttonBlockClicked(long commentId, long userId) {
		Optional<AuthUser> user = authenticationService.getAuthUserById(userId);
		user.ifPresent(authUser -> {
			view.showUserBlockWindow(userId, authUser.getLogin(), authUser.getUserRole(), commentId);
		});
	}

	@Override
	public void buttonBlockSubmitClicked(long userId, LocalDateTime blockDateTime, String blockDescription) {
		authenticationService.changeUserBlockUntilDate(userId, blockDateTime, blockDescription,
				authenticationService.getUserRole());
		view.closeUserBlockWindow();
		view.showWarning("User successfully blocked until " + blockDateTime);
	}
}
