package com.rustedbrain.study.course.view.authentication;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public interface CinemaHallConstructorView extends View {
	@Autowired
	void addListener(ViewListener viewListener);

	interface ViewListener {
		void setCinemaHallConstructorView(CinemaHallConstructorView cinemaHallConstructorView);

		void entered(ViewChangeEvent event);

		void buttonSaveCinemaHallSeatsButtonClicked();

		void addRowButtonClicked(long cinameHallId, String row, String seats, String startCoordinate);

		void deleteRowButtonClicked(long id, String rowNumber);
	}

	void addCinemaHallConstructorMenuComponents(CinemaHall cinemaHall);

	void showWarning(String message);

	void showError(String message);

	void reload();

	void setCinemaHallSeatMap(Map<Integer, List<Integer>> cinemaHallSeatCoordinateMultiMap);
}
