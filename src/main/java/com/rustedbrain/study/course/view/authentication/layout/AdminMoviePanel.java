package com.rustedbrain.study.course.view.authentication.layout;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdminMoviePanel extends Panel {

	private static final long serialVersionUID = 6960287550997036750L;
	protected VerticalLayout layout = new VerticalLayout();
	private List<ProfileView.ViewListener> listeners;
	private List<Movie> movies = new ArrayList<>();
	private Grid<Movie> grid = new Grid<>();

	public AdminMoviePanel(List<ProfileView.ViewListener> listeners, List<Movie> movies) {
		this.listeners = listeners;
		this.movies = movies;
		this.layout.addComponent(new Panel(showMovieSelectionPanel(movies)));
		setContent(this.layout);
	}

	private Component showMovieSelectionPanel(List<Movie> movies) {
		VerticalLayout mainLayout = new VerticalLayout();
		HorizontalLayout filterLayout = getFilterLayout();
		Button addNewCinemaButton = new Button("Add new movie");
		addNewCinemaButton.addClickListener(clickEvent -> {
			grid.deselectAll();
			filterLayout.addComponent(addNewMovieButtonClick());
		});

		HorizontalLayout toolbar = new HorizontalLayout(filterLayout, addNewCinemaButton);
		HorizontalLayout gridFormLayout = getGridSaveDeleteFormLayout(movies);
		mainLayout.addComponents(toolbar, gridFormLayout);
		return mainLayout;
	}

	private HorizontalLayout getGridSaveDeleteFormLayout(List<Movie> movies) {
		grid.setItems(movies);
		grid.addColumn(Movie::getOriginalName).setCaption("Original Name");
		grid.addColumn(Movie::getLocalizedName).setCaption("Localize Name");
		grid.addColumn(movie -> movie.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).getYear())
				.setCaption("Release Date");
		grid.addColumn(Movie::getCountry).setCaption("Country");
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.setSizeFull();
		SaveDeleteForm saveDeleteForm = new SaveDeleteForm();
		grid.addSelectionListener(selectionEvent -> {
			if ( grid.getSelectionModel().getFirstSelectedItem().isPresent() ) {
				saveDeleteForm.setSelectedMovie(selectionEvent.getFirstSelectedItem().get());
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

	private PopupView addNewMovieButtonClick() {
		VerticalLayout createPopupContent = new VerticalLayout();
		TextField movieLocalizeName = new TextField("Movie name");
		movieLocalizeName.setSizeFull();
		TextField movieOriginalName = new TextField("Movie original name");
		movieOriginalName.setSizeFull();
		DateTimeField movieReleaseDate = new DateTimeField("Movie release date");

		createPopupContent.addComponents(movieLocalizeName, movieOriginalName, movieReleaseDate);

		createPopupContent.addComponent(new Button("Next", (Button.ClickListener) event -> {
			listeners.forEach(viewListener -> {
				if ( !movieLocalizeName.getValue().isEmpty() ) {
					if ( !movieOriginalName.getValue().isEmpty() ) {
						if ( !movieReleaseDate.isEmpty() ) {
							viewListener.getMovieEditPresenter().buttonAddNewMovieClicked(movieLocalizeName.getValue(),
									movieOriginalName.getValue(), movieReleaseDate.getValue());
						} else {
							Notification.show("Please enter movie name", "", Notification.Type.ERROR_MESSAGE);
						}
					} else {
						Notification.show("Please enter movie original name", "", Notification.Type.ERROR_MESSAGE);
					}
				} else {
					Notification.show("Please enter movie release date", "", Notification.Type.ERROR_MESSAGE);
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
		filterTextField.setPlaceholder("Filter by original name...");
		filterTextField.setValueChangeMode(ValueChangeMode.EAGER);
		filterTextField.addValueChangeListener(event -> getFilteredByMovieOriginalName(event.getValue()));

		Button clearFilterTextButton = new Button(VaadinIcons.CLOSE);
		clearFilterTextButton.setDescription("clear the current filter");
		clearFilterTextButton.addClickListener(clickEvent -> filterTextField.clear());

		HorizontalLayout filterLayout = new HorizontalLayout(filterTextField, clearFilterTextButton);
		filterLayout.setSpacing(false);
		return filterLayout;
	}

	private void getFilteredByMovieOriginalName(String filterText) {
		if ( StringUtils.isEmpty(filterText) ) {
			grid.setItems(movies);
		} else {
			List<Movie> filteredMovies = movies.stream()
					.filter(movie -> movie.getOriginalName().toLowerCase().contains(filterText.toLowerCase()))
					.collect(Collectors.toList());
			grid.setItems(filteredMovies);
		}
	}

	private class SaveDeleteForm extends FormLayout {

		private static final long serialVersionUID = 2626488464461743216L;
		private Movie selectedMovie;
		private TextField movieOriginalNameTextField;
		private TextField movieLocalizeNameTextField;
		private TextField movieCountryTextField;
		private DateTimeField movieReleaseDateTextField;
		private Button saveButton;
		private Button deleteButton;
		private Button advancedСhangesButton;

		private SaveDeleteForm() {
			Binder<Movie> movieBinder = new Binder<Movie>();
			movieBinder.setBean(selectedMovie);
			movieReleaseDateTextField = new DateTimeField("Release Date");
			movieBinder.forField(movieReleaseDateTextField)
					.withConverter(new LocalDateTimeToDateConverter(ZoneId.systemDefault()))
					.bind(Movie::getReleaseDate, Movie::setReleaseDate);
			movieOriginalNameTextField = new TextField("Original name");
			movieBinder.forField(movieOriginalNameTextField)
					.withValidator(str -> str.length() > 0, "Movie original name can't be emty")
					.bind(Movie::getOriginalName, Movie::setOriginalName);
			movieLocalizeNameTextField = new TextField("Localize name");
			movieBinder.forField(movieLocalizeNameTextField)
					.withValidator(str -> str.length() > 0, "Movie name can't be emty")
					.bind(Movie::getLocalizedName, Movie::setLocalizedName);
			movieCountryTextField = new TextField("Country");
			advancedСhangesButton = new Button("Additional changes");
			saveButton = new Button("Save");
			deleteButton = new Button("Delete");
			saveButton.addStyleName("friendly");
			deleteButton.addStyleName("danger");
			setSizeUndefined();

			HorizontalLayout saveDeletebuttons = new HorizontalLayout(saveButton, deleteButton);
			addComponents(movieOriginalNameTextField, movieLocalizeNameTextField, movieCountryTextField,
					movieReleaseDateTextField, advancedСhangesButton, saveDeletebuttons);

			deleteButton.addClickListener(clickEvent -> {
				this.deleteMovie(this.selectedMovie);
				Notification.show("Movie deleted", "Movie name: " + this.selectedMovie.getOriginalName(),
						Notification.Type.HUMANIZED_MESSAGE);
			});
			saveButton.addClickListener(clickEvent -> {
				this.editMovie(this.selectedMovie, movieOriginalNameTextField.getValue(),
						movieLocalizeNameTextField.getValue(), movieCountryTextField.getValue(),
						movieReleaseDateTextField.getValue());
				Notification.show("Movie edit", "Movie name: " + this.selectedMovie.getOriginalName(),
						Notification.Type.HUMANIZED_MESSAGE);
			});

			advancedСhangesButton.addClickListener(clickEvent -> {
				this.advancedEditMovie(this.selectedMovie);
			});
		}

		private void advancedEditMovie(Movie selectedMovie) {
			listeners.forEach(
					listener -> listener.getMovieEditPresenter().buttonAdvancedEditMovieClicked(selectedMovie));
		}

		private void deleteMovie(Movie selectedMovie) {
			listeners.forEach(
					listener -> listener.getMovieEditPresenter().buttonDeleteMovieClicked(selectedMovie.getId()));
			this.setVisible(false);
			movies.remove(selectedMovie);
			grid.setItems(movies);
		}

		private void editMovie(Movie selectedMovie, String movieOriginalName, String movieLocalizeName,
				String movieCountry, LocalDateTime movieReleaseDate) {
			listeners.forEach(listener -> {
				listener.getMovieEditPresenter().buttonSaveMovieClicked(selectedMovie, movieOriginalName,
						movieLocalizeName, movieCountry, movieReleaseDate);
				listener.reload();
			});
		}

		public void setSelectedMovie(Movie selectedMovie) {
			this.selectedMovie = selectedMovie;
			movieOriginalNameTextField.setValue(selectedMovie.getOriginalName());
			movieLocalizeNameTextField.setValue(selectedMovie.getLocalizedName());
			movieCountryTextField.setValue(String.valueOf(selectedMovie.getCountry()));
			movieReleaseDateTextField.setValue(
					LocalDateTime.ofInstant(selectedMovie.getReleaseDate().toInstant(), ZoneId.systemDefault()));

		}
	}
}
