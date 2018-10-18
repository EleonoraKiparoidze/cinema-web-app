package com.rustedbrain.study.course.view.authentication.layout;

import java.util.ArrayList;
import java.util.List;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.model.persistence.cinema.Ticket;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class AdminStatisticPanel extends Panel {

	private static final long serialVersionUID = 9212178943806358923L;
	protected VerticalLayout layout = new VerticalLayout();
	private List<City> cities;

	public AdminStatisticPanel(List<ProfileView.ViewListener> listeners, List<City> cities) {
		this.cities = cities;
		this.layout.addComponent(new Panel(showStatisticPanel()));
		setContent(this.layout);
	}

	private Layout showStatisticPanel() {
		final VerticalLayout mainLayout = new VerticalLayout();
		Grid<Cinema> cinemasStatisticGrid = getCinemasStatisticGrid();
		Grid<Movie> moviesStatisticGrid = getMoviesStatisticGrid();
		mainLayout.addComponentsAndExpand(cinemasStatisticGrid, moviesStatisticGrid);
		return mainLayout;
	}

	private Grid<Movie> getMoviesStatisticGrid() {
		List<Movie> movies = new ArrayList<>();
		cities.forEach(city -> city.getCinemas()
				.forEach(cinema -> cinema.getFilmScreenings().forEach(fs -> movies.add(fs.getMovie()))));
		Grid<Movie> grid = new Grid<>();
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setCaptionAsHtml(true);
		grid.setCaption("<h2>Most discussing movie</h2>");
		grid.setSizeFull();
		grid.setItems(movies);
		grid.addColumn(movie -> movie.getOriginalName()).setCaption("Movie name");
		grid.addColumn(movie -> movie.getComments().stream().count()).setCaption("Comments count");
		return grid;
	}

	private Grid<Cinema> getCinemasStatisticGrid() {
		List<Cinema> cinemas = new ArrayList<>();
		cities.forEach(city -> cinemas.addAll(city.getCinemas()));
		Grid<Cinema> grid = new Grid<>();
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setCaptionAsHtml(true);
		grid.setCaption("<h2>Most popular cinema</h2>");
		grid.setSizeFull();
		grid.setItems(cinemas);
		grid.addColumn(cinema -> cinema.getName()).setCaption("Cinema");
		grid.addColumn(cinema -> cinema.getCity().getName()).setCaption("City");
		grid.addColumn(cinema -> {
			List<Ticket> tickets = new ArrayList<>();
			cinema.getFilmScreenings().forEach(fs -> {
				fs.getFilmScreeningEvents().forEach(event -> {
					tickets.addAll(event.getTickets());
				});
			});
			return tickets.stream().filter(ticket -> ticket.isReserved()).count();
		}).setCaption("Ticket");
		return grid;
	}
}
