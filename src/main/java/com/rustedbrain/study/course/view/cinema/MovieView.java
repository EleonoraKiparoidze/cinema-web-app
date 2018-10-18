package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.cinema.Comment;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

public interface MovieView extends ApplicationView {

	void showMovieInfo(Movie movie, boolean authorized, String login, boolean admin, boolean moderator);

	@Autowired
	void addCinemaViewListener(Listener listener);

	interface Listener {

		void entered(ViewChangeListener.ViewChangeEvent event);

		void setView(MovieView view);

		void buttonProfileClicked(long id);

		void buttonCreateMessageClicked(String textArea);

		void buttonDeleteCommentClicked(long id);

		void buttonEditCommentClicked(long commentId, String newValue);

		void buttonPlusClicked(Comment comment);

		void buttonMinusClicked(Comment comment);

		void buttonBlockAndDeleteClicked(long commentId, long userId);

		void buttonBlockClicked(long commentId, long userId);

		void buttonBlockSubmitClicked(long userId, LocalDateTime blockDateTime, String blockDescription);
	}

	void showUserBlockWindow(long userId, String login, UserRole userRole, long commentId);

	void closeUserBlockWindow();

	void showUserBlockDeleteWindow(long userId, String login, UserRole userRole, long commentId);
}
