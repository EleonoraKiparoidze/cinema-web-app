package com.rustedbrain.study.course.view.authentication.layout;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.rustedbrain.study.course.view.authentication.ProfileView.ViewListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdminFilmScreeningPanel extends Panel {

	private static final long serialVersionUID = 6730582989061302558L;

	protected VerticalLayout layout = new VerticalLayout();
	private List<ProfileView.ViewListener> listeners;
	private Set<FilmScreening> filmScreenings = new HashSet<>();
	private List<City> cities = new ArrayList<>();
	private List<Movie> movies = new ArrayList<>();
	private Grid<FilmScreening> grid = new Grid<>();
	private Set<Cinema> cinemas = new HashSet<>();;

	public AdminFilmScreeningPanel(List<ViewListener> listeners, List<City> cities, List<Movie> movies) {
		this.listeners = listeners;
		this.cities = cities;
		this.movies = movies;
		this.cities.forEach(city -> cinemas.addAll(city.getCinemas()));
		this.cinemas.forEach(cinema -> filmScreenings.addAll(cinema.getFilmScreenings()));
		this.layout.addComponent(new Panel(showFilmScreeningsSelectionPanel(filmScreenings)));
		setContent(this.layout);
	}

	private Layout showFilmScreeningsSelectionPanel(Set<FilmScreening> filmScreenings) {
		VerticalLayout mainLayout = new VerticalLayout();
		HorizontalLayout gridFormLayout = getGridSaveDeleteFormLayout(filmScreenings);
		HorizontalLayout filterLayout = getFilterLayout();

		Button addNewFilmScreening = new Button("Add new film screening");
		addNewFilmScreening.addClickListener(clickEvent -> {
			grid.deselectAll();
			filterLayout.addComponent(addNewFilmScreeningButtonClick());
		});

		HorizontalLayout toolbar = new HorizontalLayout(filterLayout, addNewFilmScreening);
		mainLayout.addComponents(toolbar, gridFormLayout);
		return mainLayout;
	}

	private HorizontalLayout getGridSaveDeleteFormLayout(Set<FilmScreening> filmScreenings) {
		grid.setItems(filmScreenings);
		grid.addColumn(filmScreening -> filmScreening.getMovie().getLocalizedName()).setCaption("Movie");
		grid.addColumn(filmScreening -> filmScreening.getCinema().getName()).setCaption("Cinema");
		grid.addColumn(filmScreening -> filmScreening.getCinema().getCity().getName()).setCaption("City");
		grid.addColumn(filmScreening -> filmScreening.getStartDate()).setCaption("Start date");
		grid.addColumn(filmScreening -> filmScreening.getEndDate()).setCaption("End date");
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.setSizeFull();
		SaveDeleteForm saveDeleteForm = new SaveDeleteForm();

		grid.addSelectionListener(selectionEvent -> {
			if ( grid.getSelectionModel().getFirstSelectedItem().isPresent() ) {
				saveDeleteForm.setSelectedFilmScreening(selectionEvent.getFirstSelectedItem().get());
				saveDeleteForm.setVisible(true);
			} else {
				saveDeleteForm.setVisible(false);
			}
		});
		saveDeleteForm.setVisible(false);

		HorizontalLayout gridFormLayout = new HorizontalLayout(grid, saveDeleteForm);
		gridFormLayout.setSizeFull();
		gridFormLayout.setExpandRatio(grid, 1);
		return gridFormLayout;
	}

	private PopupView addNewFilmScreeningButtonClick() {
		VerticalLayout createPopupContent = new VerticalLayout();

		ComboBox<Movie> movieComboBox = new ComboBox<>("Movie");
		movieComboBox.setEmptySelectionAllowed(false);
		movieComboBox.setItems(movies);
		movieComboBox.setItemCaptionGenerator(Movie::getLocalizedName);
		movieComboBox.setSelectedItem(movies.get(0));
		movieComboBox.setSizeFull();
		ComboBox<City> cityComboBox = new ComboBox<>("City");
		ComboBox<Cinema> cinemaComboBox = new ComboBox<>("Cinema");

		cityComboBox.setEmptySelectionAllowed(false);
		cityComboBox.setItems(cities);
		cityComboBox.setItemCaptionGenerator(City::getName);
		cityComboBox.setSelectedItem(cities.get(0));
		cityComboBox.setSizeFull();
		cityComboBox.addValueChangeListener(valueChangeEvent -> {
			Set<Cinema> cinemas = valueChangeEvent.getValue().getCinemas();
			cinemaComboBox.setEmptySelectionAllowed(false);
			cinemaComboBox.setItems(cinemas);
			cinemaComboBox.setItemCaptionGenerator(Cinema::getName);
			cinemaComboBox.setSelectedItem(cinemas.iterator().next());
		});
		cinemaComboBox.setEmptySelectionAllowed(false);
		cinemaComboBox.setItems(cinemas.stream().filter(cinema -> cinema.getCity().equals(cities.get(0))));
		cinemaComboBox.setItemCaptionGenerator(Cinema::getName);
		cinemaComboBox.setSizeFull();
		DateTimeField filmScreeningStartDateTextField = new DateTimeField("Start date");
		DateTimeField filmScreeningEndDateTextField = new DateTimeField("End date");
		createPopupContent.addComponents(movieComboBox, cityComboBox, cinemaComboBox, filmScreeningStartDateTextField,
				filmScreeningEndDateTextField);

		createPopupContent.addComponent(new Button("Add", (Button.ClickListener) event -> {
			listeners.forEach(listener -> {
				if ( movieComboBox.getSelectedItem().isPresent() ) {
					if ( cityComboBox.getSelectedItem().isPresent() ) {
						if ( cinemaComboBox.getSelectedItem().isPresent() ) {
							if ( filmScreeningStartDateTextField.getValue()
									.isBefore(filmScreeningEndDateTextField.getValue()) ) {
								listener.getFilmScreeningEditPresenter().buttonAddNewFilmScreeningClicked(
										movieComboBox.getSelectedItem(), cinemaComboBox.getSelectedItem(),
										filmScreeningStartDateTextField.getValue(),
										filmScreeningEndDateTextField.getValue());
								listener.reload();
							} else {
								Notification.show("End date must be after start date", "",
										Notification.Type.ERROR_MESSAGE);
							}
						} else {
							Notification.show("Please select cinema", "", Notification.Type.ERROR_MESSAGE);
						}
					} else {
						Notification.show("Please select City", "", Notification.Type.ERROR_MESSAGE);
					}
				} else {
					Notification.show("Please select movie", "", Notification.Type.ERROR_MESSAGE);
				}
			});
		}));

		PopupView createPopup = new PopupView(null, createPopupContent);
		createPopup.setSizeUndefined();
		createPopup.setPopupVisible(true);
		return createPopup;
	}

	private HorizontalLayout getFilterLayout() {
		TextField filterTextField = new TextField();
		filterTextField.setPlaceholder("Filter by Movie name...");
		filterTextField.setValueChangeMode(ValueChangeMode.EAGER);
		filterTextField.addValueChangeListener(event -> getFilteredByMovieName(event.getValue()));

		Button clearFilterTextButton = new Button(VaadinIcons.CLOSE);
		clearFilterTextButton.setDescription("clear the current filter");
		clearFilterTextButton.addClickListener(clickEvent -> filterTextField.clear());

		HorizontalLayout filterLayout = new HorizontalLayout(filterTextField, clearFilterTextButton);
		filterLayout.setSpacing(false);
		return filterLayout;
	}

	private void getFilteredByMovieName(String filterText) {
		if ( StringUtils.isEmpty(filterText) ) {
			grid.setItems(filmScreenings);
		} else {
			List<FilmScreening> filteredFilmScreening =
					filmScreenings.stream().filter(filmScreening -> filmScreening.getMovie().getLocalizedName()
							.toLowerCase().contains(filterText.toLowerCase())).collect(Collectors.toList());
			grid.setItems(filteredFilmScreening);
		}
		;
	}

	private class SaveDeleteForm extends FormLayout {

		private static final long serialVersionUID = -7615827374648626019L;
		private FilmScreening selectedFilmScreening;
		private ComboBox<Movie> movieComboBox;
		private ComboBox<Cinema> cinemaComboBox;
		private ComboBox<City> cityComboBox;
		private DateTimeField filmScreeningStartDateTextField;
		private DateTimeField filmScreeningEndDateTextField;
		private Button saveButton;
		private Button deleteButton;
		private Button filmScreeningEventsButton;

		private SaveDeleteForm() {
			filmScreeningStartDateTextField = new DateTimeField("Start date");
			filmScreeningEndDateTextField = new DateTimeField("End date");
			movieComboBox = new ComboBox<>("Movie");
			cinemaComboBox = new ComboBox<>("Cinema");
			cityComboBox = new ComboBox<>("City");
			filmScreeningEventsButton = new Button("Film Screening Events");
			saveButton = new Button("Save");
			deleteButton = new Button("Delete");
			saveButton.addStyleName("friendly");
			deleteButton.addStyleName("danger");
			setSizeUndefined();

			HorizontalLayout saveDeleteButtons = new HorizontalLayout(saveButton, deleteButton);
			addComponents(movieComboBox, cityComboBox, cinemaComboBox, filmScreeningStartDateTextField,
					filmScreeningEndDateTextField, filmScreeningEventsButton, saveDeleteButtons);

			deleteButton.addClickListener(clickEvent -> {
				this.deleteFilmScreening(this.selectedFilmScreening);
				Notification.show("Film Screening deleted",
						"Movie name: " + this.selectedFilmScreening.getMovie().getLocalizedName(),
						Notification.Type.HUMANIZED_MESSAGE);
			});

			saveButton.addClickListener(clickEvent -> {
				this.editFilmScreening(this.selectedFilmScreening, movieComboBox.getSelectedItem().get(),
						cinemaComboBox.getSelectedItem().get(), filmScreeningStartDateTextField.getValue(),
						filmScreeningEndDateTextField.getValue());
			});

			filmScreeningEventsButton.addClickListener(clickEvent -> {
				this.editFilmScreeningEventsButtonClicked(this.selectedFilmScreening);
			});
		}

		private void editFilmScreeningEventsButtonClicked(FilmScreening selectedFilmScreening) {
			listeners.forEach(listener -> listener.buttonFilmScreeningEventsClicked(selectedFilmScreening));
		}

		private void editFilmScreening(FilmScreening selectedFilmScreening, Movie movie, Cinema cinema,
				LocalDateTime startDate, LocalDateTime endDate) {
			listeners.forEach(listener -> {
				listener.getFilmScreeningEditPresenter().buttonSaveFilmScreeningClicked(selectedFilmScreening, movie,
						cinema, startDate, endDate);
				listener.reload();
			});
		}

		private void deleteFilmScreening(FilmScreening selectedFilmScreening) {
			listeners.forEach(listener -> listener.getFilmScreeningEditPresenter()
					.buttonDeleteFilmScreeningClicked(selectedFilmScreening.getId()));
			this.setVisible(false);
			filmScreenings.remove(selectedFilmScreening);
			grid.setItems(filmScreenings);
		}

		public void setSelectedFilmScreening(FilmScreening selectedFilmScreening) {
			this.selectedFilmScreening = selectedFilmScreening;

			movieComboBox.setItems(movies);
			movieComboBox.setItemCaptionGenerator(Movie::getLocalizedName);
			movieComboBox.setEmptySelectionAllowed(false);
			movieComboBox.setSelectedItem(selectedFilmScreening.getMovie());
			cityComboBox.setItems(cities);
			cityComboBox.setItemCaptionGenerator(City::getName);
			cityComboBox.setEmptySelectionAllowed(false);
			cityComboBox.setSelectedItem(selectedFilmScreening.getCinema().getCity());
			cityComboBox.addValueChangeListener(valueChangeEvent -> {
				Set<Cinema> cinemas = valueChangeEvent.getValue().getCinemas();
				cinemaComboBox.setEmptySelectionAllowed(false);
				cinemaComboBox.setItems(cinemas);
				cinemaComboBox.setItemCaptionGenerator(Cinema::getName);
				cinemaComboBox.setSelectedItem(cinemas.iterator().next());
			});

			cinemaComboBox.setSelectedItem(selectedFilmScreening.getCinema());
			cinemaComboBox.setItemCaptionGenerator(Cinema::getName);
			cinemaComboBox.setEmptySelectionAllowed(false);

			filmScreeningStartDateTextField.setValue(
					LocalDateTime.ofInstant(selectedFilmScreening.getStartDate().toInstant(), ZoneId.systemDefault()));
			filmScreeningEndDateTextField.setValue(
					LocalDateTime.ofInstant(selectedFilmScreening.getEndDate().toInstant(), ZoneId.systemDefault()));

		}

	}

}
