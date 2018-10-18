package com.rustedbrain.study.course.view.authentication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.persistence.cinema.Actor;
import com.rustedbrain.study.course.model.persistence.cinema.Genre;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = VaadinUI.MOVIE_EDIT_VIEW)
public class MovieAdvancedEditViewImpl extends VerticalLayout implements MovieAdvancedEditView {

	private static final long serialVersionUID = -3386283372124579119L;
	private static final String POSTER_PATH = "C:\\Users\\User\\Downloads\\";
	private static final int POSTER_IMAGE_HEIGHT = 200;
	private static final int POSTER_IMAGE_WIDTH = 150;
	private static final int TRAILER_WINDOW_HEIGHT = 360;
	private static final int TRAILER_WINDOW_WIDTH = 480;
	private static final List<String> ALLOWED_MIME_TYPES_LIST = Arrays.asList("image/jpeg", "image/png", "image/jpg");

	private Movie editedMovie;
	private TabSheet tabSheet;
	private List<MovieAdvancedEditView.ViewListener> viewListeners = new ArrayList<>();
	private Panel movieInfoPanel;
	private List<Actor> actors;
	private List<Genre> genres;

	@Autowired
	public MovieAdvancedEditViewImpl(AuthenticationService authenticationService) {
		addComponentsAndExpand(new Panel(new MenuComponent(authenticationService)));
		tabSheet = new TabSheet();
		tabSheet.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
		addComponentsAndExpand(tabSheet);
	}

	@Override
	@Autowired
	public void addListener(ViewListener viewListener) {
		viewListener.setMovieEditView(this);
		viewListeners.add(viewListener);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		viewListeners.forEach(viewListener -> viewListener.entered(event));
	}

	private Panel getMovieInfoPanel() {
		if ( movieInfoPanel == null ) {
			movieInfoPanel = new Panel();
		}
		return movieInfoPanel;
	}

	@Override
	public void setMovieInformation(Movie movie, List<Actor> actors, List<Genre> genres) {
		this.editedMovie = movie;
		this.actors = actors;
		this.genres = genres;
		GridLayout movieInfoEditLayout = new GridLayout();
		movieInfoEditLayout.setColumns(2);
		movieInfoEditLayout.setSizeFull();
		movieInfoEditLayout.setMargin(true);

		VerticalLayout infoLayout = getMovieInfoLayout();
		Panel movieInfoPanel = getMovieInfoPanel();
		movieInfoPanel.setContent(infoLayout);
		movieInfoEditLayout.addComponent(movieInfoPanel, 0, 0);

		VerticalLayout editLayout = getMovieEditLayout();
		movieInfoEditLayout.addComponent(editLayout, 1, 0);

		movieInfoEditLayout.setColumnExpandRatio(0, 1);
		movieInfoEditLayout.setColumnExpandRatio(1, 2);
		tabSheet.addTab(movieInfoEditLayout, "Movie edit");

	}

	private VerticalLayout getMovieInfoLayout() {
		VerticalLayout infoLayout = new VerticalLayout();
		Image image = new Image("<h1><b>" + editedMovie.getLocalizedName() + "</b></h1>",
				new FileResource(new File(editedMovie.getPosterPath())));
		image.setCaptionAsHtml(true);
		image.setAlternateText("POSTER");
		image.setWidth(POSTER_IMAGE_WIDTH, Unit.PIXELS);
		image.setHeight(POSTER_IMAGE_HEIGHT, Unit.PIXELS);
		infoLayout.addComponent(image);
		Label originalNameLabel = new Label("Original title: " + editedMovie.getOriginalName());
		originalNameLabel.setSizeFull();
		infoLayout.addComponent(originalNameLabel);
		Label countryLabel = new Label("Country: " + editedMovie.getCountry());
		countryLabel.setSizeFull();
		infoLayout.addComponent(countryLabel);
		Label releaseDateLabel =
				new Label("Year: " + editedMovie.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).getYear());
		releaseDateLabel.setSizeFull();
		infoLayout.addComponent(releaseDateLabel);
		Label minAge = new Label("Age: " + editedMovie.getMinAge() + "+");
		minAge.setSizeFull();
		infoLayout.addComponent(minAge);
		Label producer = new Label("Producer: " + String.valueOf(editedMovie.getProducer()));
		producer.setSizeFull();
		infoLayout.addComponent(producer);
		List<String> genresName = new ArrayList<>();
		editedMovie.getGenres().forEach(genre -> genresName.add(genre.getName()));
		Label genres = new Label("Genres: " + genresName.toString());
		genres.setSizeFull();
		infoLayout.addComponent(genres);
		Label time = new Label("Time: " + String.valueOf(editedMovie.getTimeMinutes()));
		time.setSizeFull();
		infoLayout.addComponent(time);
		List<String> actorsName = new ArrayList<>();
		editedMovie.getActors().forEach(actor -> actorsName.add(actor.getName() + " " + actor.getSurname()));
		Label actors = new Label("Starring: " + actorsName.toString());
		actors.setSizeFull();
		infoLayout.addComponent(actors);
		Label trailerURL = new Label("Trailer URL: " + editedMovie.getTrailerURL());
		trailerURL.setSizeFull();
		infoLayout.addComponent(trailerURL);
		Label descriptionLabel = new Label(editedMovie.getDescription());
		descriptionLabel.setSizeFull();
		infoLayout.addComponent(descriptionLabel);
		return infoLayout;
	}

	private VerticalLayout getMovieEditLayout() {
		VerticalLayout editLayout = new VerticalLayout();
		Binder<Movie> movieBinder = new Binder<Movie>();
		movieBinder.setBean(editedMovie);

		TextField localizedNameTextField = getLocalizeNameTextField(movieBinder);
		TextField minAgeTextField = getMinAgeTextField(movieBinder);
		TextField originalNameTextField = getOriginalNameTextField(movieBinder);
		TextField producerTextField = getProducerTextField(movieBinder);
		DateTimeField releaseDateTextField = getReleaseDateTextField(movieBinder);
		VerticalLayout genresInfoEditLayout = getGenresInfoEditLayout();
		TextField timeTextField = getTimeTextField(movieBinder);
		TextField countryTextField = getCountryTextField(movieBinder);
		VerticalLayout actorsInfoEditLayout = getActorsInfoEditLayout();
		TextArea descriptionTextArea = getDescriptionTextArea(movieBinder);
		HorizontalLayout posterPathInfoLoadLayout = getPosterPathInfoLoadLayout(movieBinder);
		VerticalLayout trailerURLVerticalLayout = getTrailerURLLayout();
		editLayout.addComponents(localizedNameTextField, minAgeTextField, originalNameTextField, producerTextField,
				releaseDateTextField, genresInfoEditLayout, timeTextField, countryTextField, actorsInfoEditLayout,
				descriptionTextArea, posterPathInfoLoadLayout, trailerURLVerticalLayout);

		Button saveButton = new Button("Save");
		saveButton.addStyleName("friendly");
		saveButton.addClickListener(clickEvent -> UI.getCurrent().addWindow(getSaveMovieConformationWindow()));
		editLayout.addComponent(saveButton);
		editLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
		return editLayout;
	}

	private Window getSaveMovieConformationWindow() {
		Window saveMovieConformationWindow = new Window();
		saveMovieConformationWindow.setHeight("100px");
		saveMovieConformationWindow.setWidth("180px");
		saveMovieConformationWindow.setModal(true);
		saveMovieConformationWindow.setResizable(false);
		saveMovieConformationWindow.setDraggable(false);

		Label saveLabel = new Label("Save Movie?");
		saveLabel.setSizeFull();
		Button saveButton = new Button("Save", (Button.ClickListener) event -> {
			viewListeners.forEach(listener -> listener.buttonSaveMovieButtonClicked(editedMovie));
			saveMovieConformationWindow.close();
			showNotification("Movie saved");
		});
		saveButton.addStyleName("friendly");

		VerticalLayout saveButtonLayout = new VerticalLayout();
		saveButtonLayout.addComponentsAndExpand(saveLabel, saveButton);
		saveMovieConformationWindow.setContent(saveButtonLayout);

		return saveMovieConformationWindow;
	}

	private HorizontalLayout getPosterPathInfoLoadLayout(Binder<Movie> movieBinder) {
		HorizontalLayout posterPathInfoLoadLayout = new HorizontalLayout();
		posterPathInfoLoadLayout.setSizeFull();
		posterPathInfoLoadLayout.setSpacing(false);

		TextField posterPathTextField = new TextField("Poster path");
		movieBinder.forField(posterPathTextField).withValidator(str -> str.length() > 0, "Poster path can't be emty")
				.bind(Movie::getPosterPath, Movie::setPosterPath);
		posterPathTextField.setValue(editedMovie.getPosterPath());
		posterPathTextField.setSizeFull();

		final Image uploadedImage = new Image("Uploaded image");
		ImageUploader receiver = new ImageUploader(uploadedImage);
		uploadedImage.setVisible(false);
		Upload uploadPoster = new Upload("", receiver);
		uploadPoster.addSucceededListener(receiver);
		uploadPoster.addStartedListener(new StartedListener() {
			private static final long serialVersionUID = -7552215584009656896L;

			@Override
			public void uploadStarted(StartedEvent event) {
				String contentType = event.getMIMEType();
				boolean allowed = false;
				for (String allowedMimeType : ALLOWED_MIME_TYPES_LIST) {
					if ( contentType.equalsIgnoreCase(allowedMimeType) ) {
						allowed = true;
						break;
					}
				}
				if ( allowed ) {
					posterPathTextField.setValue(POSTER_PATH + event.getFilename());
					uploadPoster.setEnabled(true);
				} else {
					showError("Error! Allowed MIME: " + ALLOWED_MIME_TYPES_LIST.toString());
					uploadPoster.interruptUpload();
				}
			}
		});

		posterPathInfoLoadLayout.addComponents(uploadedImage, posterPathTextField, uploadPoster);
		posterPathInfoLoadLayout.setExpandRatio(posterPathTextField, 1);
		posterPathInfoLoadLayout.setExpandRatio(uploadPoster, 0);
		return posterPathInfoLoadLayout;
	}

	private VerticalLayout getGenresInfoEditLayout() {
		editedMovie.setGenres(editedMovie.getGenres());

		VerticalLayout genresInfoEditLayout = new VerticalLayout();
		genresInfoEditLayout.setMargin(false);
		genresInfoEditLayout.setSpacing(false);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(false);

		TextField genresTextField = new TextField();
		genresTextField.setEnabled(false);
		genresTextField.setSizeFull();

		List<String> genresName = new ArrayList<>();
		editedMovie.getGenres().forEach(genre -> genresName.add(genre.getName()));
		genresTextField.setValue(genresName.toString());

		Button editGenresButton = new Button("Edit");
		editGenresButton.addClickListener(clickEvent -> {
			UI.getCurrent().addWindow(getGenresWindow(genresTextField));
		});

		horizontalLayout.addComponents(genresTextField, editGenresButton);
		horizontalLayout.setExpandRatio(genresTextField, 1);
		horizontalLayout.setExpandRatio(editGenresButton, 0);
		Label genresLabel = new Label("Genres");
		genresInfoEditLayout.addComponentsAndExpand(genresLabel, horizontalLayout);
		return genresInfoEditLayout;
	}

	private Window getGenresWindow(TextField genresTextField) {
		Window genresWindow = new Window("Genres");
		genresWindow.setWidth("600px");
		genresWindow.setModal(true);
		genresWindow.setResizable(true);
		genresWindow.setDraggable(false);

		Grid<Genre> genresGrid = new Grid<>();
		genresGrid.setSizeFull();
		genresGrid.setSelectionMode(SelectionMode.MULTI);
		genresGrid.setItems(genres);
		genresGrid.addColumn(Genre::getName).setCaption("Name");
		VerticalLayout verticalLayout = new VerticalLayout();
		genresGrid.addColumn(genre -> "Edit", new ButtonRenderer<Genre>(clickEvent -> {
			verticalLayout.addComponent(editGenreButtonClick(genresGrid, genres, clickEvent.getItem()));
		}));

		editedMovie.getGenres().forEach(genre -> genresGrid.select(genre));
		Button buttonSave = new Button("Save");
		buttonSave.addClickListener(listener -> {
			editedMovie.setGenres(genresGrid.getSelectedItems());
			List<String> genresName = new ArrayList<>();
			editedMovie.getGenres().forEach(genre -> genresName.add(genre.getName()));
			genresTextField.setValue(genresName.toString());
			genresWindow.close();
		});
		buttonSave.addStyleName("friendly");

		Button addFeature = new Button("Add new genre");
		addFeature.addClickListener(e -> {
			verticalLayout.addComponent(addNewGenreButtonClick(genresGrid, genres));
		});

		verticalLayout.addComponents(new Panel(new VerticalLayout(genresGrid, addFeature)), buttonSave);
		genresWindow.setContent(verticalLayout);
		return genresWindow;
	}

	private PopupView editGenreButtonClick(Grid<Genre> genresGrid, List<Genre> genres, Genre genre) {
		VerticalLayout createPopupContent = new VerticalLayout();
		TextField genreNameTextField = new TextField("New name");
		genreNameTextField.setValue(genre.getName().isEmpty() ? "" : genre.getName());
		createPopupContent.addComponents(genreNameTextField);

		createPopupContent.addComponent(new Button("Edit", (Button.ClickListener) event -> {
			viewListeners.forEach(listener -> {
				if ( !genreNameTextField.getValue().isEmpty() ) {
					listener.buttonEditGenreClicked(genre.getId(), genreNameTextField.getValue());
					reload();
				} else {
					showError("Please enter genre name");
				}
			});
		}));

		PopupView popup = new PopupView(null, createPopupContent);
		popup.setSizeUndefined();
		popup.setPopupVisible(true);
		return popup;
	}

	private PopupView addNewGenreButtonClick(Grid<Genre> genresGrid, List<Genre> genres) {
		VerticalLayout createPopupContent = new VerticalLayout();

		TextField genreNameTextField = new TextField("Name");
		createPopupContent.addComponents(genreNameTextField);

		createPopupContent.addComponent(new Button("Add", (Button.ClickListener) event -> {
			viewListeners.forEach(listener -> {
				if ( !genreNameTextField.getValue().isEmpty() ) {
					listener.buttonAddNewGenreClicked(genreNameTextField.getValue());
					genres.add(new Genre(genreNameTextField.getValue()));
					genresGrid.setItems(genres);
					reload();
				} else {
					showError("Please enter genre name");
				}
			});
		}));

		PopupView popup = new PopupView(null, createPopupContent);
		popup.setSizeUndefined();
		popup.setPopupVisible(true);
		return popup;
	}

	private VerticalLayout getActorsInfoEditLayout() {
		editedMovie.setActors(editedMovie.getActors());

		VerticalLayout actorsInfoEditLayout = new VerticalLayout();
		actorsInfoEditLayout.setMargin(false);
		actorsInfoEditLayout.setSpacing(false);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(false);

		TextField actorsTextField = new TextField();
		actorsTextField.setEnabled(false);
		actorsTextField.setSizeFull();
		List<String> actorsName = new ArrayList<>();
		editedMovie.getActors().forEach(actor -> actorsName.add(actor.getName() + " " + actor.getSurname()));
		actorsTextField.setValue(actorsName.toString());

		Button editActorsButton = new Button("Edit");
		editActorsButton.addClickListener(clickEvent -> {
			UI.getCurrent().addWindow(getActorsWindow(actorsTextField));
		});

		horizontalLayout.addComponents(actorsTextField, editActorsButton);
		horizontalLayout.setExpandRatio(actorsTextField, 1);
		horizontalLayout.setExpandRatio(editActorsButton, 0);

		Label actorsLabel = new Label("Starring");

		actorsInfoEditLayout.addComponentsAndExpand(actorsLabel, horizontalLayout);
		return actorsInfoEditLayout;
	}

	private Window getActorsWindow(TextField actorsTextField) {
		Window actorsWindow = new Window("Actors");
		actorsWindow.setWidth("600px");
		actorsWindow.setModal(true);
		actorsWindow.setResizable(true);
		actorsWindow.setDraggable(false);

		Grid<Actor> actorGrid = new Grid<>();
		actorGrid.setSizeFull();
		actorGrid.setSelectionMode(SelectionMode.MULTI);
		actorGrid.setItems(actors);
		actorGrid.addColumn(Actor::getName).setCaption("Name");
		actorGrid.addColumn(Actor::getSurname).setCaption("Surname");
		VerticalLayout verticalLayout = new VerticalLayout();
		actorGrid.addColumn(actor -> "Edit", new ButtonRenderer<Actor>(clickEvent -> {
			verticalLayout.addComponent(editActorButtonClick(actorGrid, actors, clickEvent.getItem()));
		}));

		editedMovie.getActors().forEach(actor -> actorGrid.select(actor));

		Button buttonSave = new Button("Save");
		buttonSave.addClickListener(listener -> {
			editedMovie.setActors(actorGrid.getSelectedItems());
			List<String> actorsName = new ArrayList<>();
			editedMovie.getActors().forEach(actor -> actorsName.add(actor.getName() + " " + actor.getSurname()));
			actorsTextField.setValue(actorsName.toString());
			actorsWindow.close();
		});
		buttonSave.addStyleName("friendly");

		Button addActor = new Button("Add new actor");
		addActor.addClickListener(e -> {
			verticalLayout.addComponent(addNewActorButtonClick(actorGrid, actors));
		});

		verticalLayout.addComponents(new Panel(new VerticalLayout(actorGrid, addActor)), buttonSave);
		actorsWindow.setContent(verticalLayout);
		return actorsWindow;
	}

	private PopupView editActorButtonClick(Grid<Actor> actorGrid, List<Actor> actors, Actor actor) {
		VerticalLayout createPopupContent = new VerticalLayout();

		TextField actorNameTextField = new TextField("New name");
		actorNameTextField.setValue(actor.getName().isEmpty() ? " " : actor.getName());
		TextField actorSurnameTextField = new TextField("New Surname");
		actorSurnameTextField.setValue(actor.getSurname().isEmpty() ? " " : actor.getSurname());
		createPopupContent.addComponents(actorNameTextField, actorSurnameTextField);

		createPopupContent.addComponent(new Button("Edit", (Button.ClickListener) event -> {
			viewListeners.forEach(listener -> {
				if ( !actorNameTextField.getValue().isEmpty() ) {
					listener.buttonEditActorClicked(actor.getId(), actorNameTextField.getValue(),
							actorSurnameTextField.getValue());
					reload();
				} else {
					showError("Please enter actor name");
				}
			});
		}));

		PopupView popup = new PopupView(null, createPopupContent);
		popup.setSizeUndefined();
		popup.setPopupVisible(true);
		return popup;
	}

	private PopupView addNewActorButtonClick(Grid<Actor> actorGrid, List<Actor> actors) {
		VerticalLayout createPopupContent = new VerticalLayout();

		TextField actorNameTextField = new TextField("Name");
		TextField actorSurnameTextField = new TextField("Surname");

		createPopupContent.addComponents(actorNameTextField, actorSurnameTextField);

		createPopupContent.addComponent(new Button("Add", (Button.ClickListener) event -> {
			viewListeners.forEach(listener -> {
				if ( !actorNameTextField.getValue().isEmpty() ) {
					listener.buttonAddNewActorClicked(actorNameTextField.getValue(), actorSurnameTextField.getValue());
					actors.add(new Actor(actorNameTextField.getValue(), actorSurnameTextField.getValue()));
					actorGrid.setItems(actors);
					reload();
				} else {
					showError("Please enter actor name");
				}
			});
		}));

		PopupView popup = new PopupView(null, createPopupContent);
		popup.setSizeUndefined();
		popup.setPopupVisible(true);
		return popup;
	}

	private VerticalLayout getTrailerURLLayout() {
		VerticalLayout trailerURLLayout = new VerticalLayout();
		trailerURLLayout.setMargin(false);
		trailerURLLayout.setSpacing(false);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(false);

		TextField trailerURLTextField = new TextField();
		trailerURLTextField.setValue(editedMovie.getTrailerURL());
		trailerURLTextField.setSizeFull();

		Button showTrailerButton = new Button("Show");
		Embedded e = new Embedded(editedMovie.getLocalizedName(), new ExternalResource(trailerURLTextField.getValue()));
		e.setMimeType("application/x-shockwave-flash");
		e.setSizeFull();
		showTrailerButton
				.addClickListener((Button.ClickListener) event -> showTrailerWindow(editedMovie.getLocalizedName(), e));
		trailerURLTextField.addValueChangeListener(listener -> {
			e.setSource(new ExternalResource(listener.getValue()));
		});

		horizontalLayout.addComponents(trailerURLTextField, showTrailerButton);
		horizontalLayout.setExpandRatio(trailerURLTextField, 1);
		horizontalLayout.setExpandRatio(showTrailerButton, 0);
		Label trailerURLLabel = new Label("Trailer URL");
		trailerURLLayout.addComponentsAndExpand(trailerURLLabel, horizontalLayout);
		return trailerURLLayout;
	}

	private void showTrailerWindow(String movieName, Embedded embedded) {
		Window trailerWindow = new Window(movieName);
		/* trailerWindow.addCloseListener((Window.CloseListener) e -> trailerWindow = null); */
		trailerWindow.setModal(true);
		trailerWindow.setDraggable(false);
		trailerWindow.setContent(embedded);
		trailerWindow.setWidth(TRAILER_WINDOW_WIDTH, Unit.PIXELS);
		trailerWindow.setHeight(TRAILER_WINDOW_HEIGHT, Unit.PIXELS);
		UI.getCurrent().addWindow(trailerWindow);
	}

	private TextArea getDescriptionTextArea(Binder<Movie> movieBinder) {
		TextArea descriptionTextArea = new TextArea("Description");
		movieBinder.forField(descriptionTextArea).withValidator(str -> str.length() > 0, "Description can't be emty")
				.bind(Movie::getDescription, Movie::setDescription);
		descriptionTextArea.setSizeFull();
		descriptionTextArea.setValue(editedMovie.getDescription());
		return descriptionTextArea;
	}

	private TextField getCountryTextField(Binder<Movie> movieBinder) {
		TextField countryTextField = new TextField("Country");
		movieBinder.forField(countryTextField).bind(Movie::getCountry, Movie::setCountry);
		countryTextField.setSizeFull();
		countryTextField.setValue(String.valueOf(editedMovie.getCountry()));
		return countryTextField;
	}

	private TextField getTimeTextField(Binder<Movie> movieBinder) {
		TextField timeTextField = new TextField("Time");
		movieBinder.forField(timeTextField).withConverter(new StringToIntegerConverter("Must be Integer"))
				.bind(Movie::getTimeMinutes, Movie::setTimeMinutes);
		timeTextField.setSizeFull();
		timeTextField.setValue(String.valueOf(editedMovie.getTimeMinutes()));
		return timeTextField;
	}

	private DateTimeField getReleaseDateTextField(Binder<Movie> movieBinder) {
		DateTimeField releaseDateTextField = new DateTimeField("Release date");
		movieBinder.forField(releaseDateTextField)
				.withConverter(new LocalDateTimeToDateConverter(ZoneId.systemDefault()))
				.bind(Movie::getReleaseDate, Movie::setReleaseDate);
		releaseDateTextField.setSizeFull();
		releaseDateTextField
				.setValue(LocalDateTime.ofInstant(editedMovie.getReleaseDate().toInstant(), ZoneId.systemDefault()));
		return releaseDateTextField;
	}

	private TextField getProducerTextField(Binder<Movie> movieBinder) {
		TextField producerTextField = new TextField("Producer");
		movieBinder.forField(producerTextField).bind(Movie::getProducer, Movie::setProducer);
		producerTextField.setSizeFull();
		producerTextField.setValue(String.valueOf(editedMovie.getProducer()));
		return producerTextField;
	}

	private TextField getOriginalNameTextField(Binder<Movie> movieBinder) {
		TextField originalNameTextField = new TextField("Original title");
		movieBinder.forField(originalNameTextField)
				.withValidator(str -> str.length() > 0, "Movie original name can't be emty")
				.bind(Movie::getOriginalName, Movie::setOriginalName);
		originalNameTextField.setValue(editedMovie.getOriginalName());
		originalNameTextField.setSizeFull();
		return originalNameTextField;
	}

	private TextField getMinAgeTextField(Binder<Movie> movieBinder) {
		TextField minAgeTextField = new TextField("Age");
		movieBinder.forField(minAgeTextField).withValidator(str -> str.length() < 4, "Must be less than 4 chars")
				.withConverter(new StringToIntegerConverter("Must be Integer"))
				.bind(Movie::getMinAge, Movie::setMinAge);
		minAgeTextField.setValue(String.valueOf(editedMovie.getMinAge()));
		minAgeTextField.setSizeFull();
		return minAgeTextField;
	}

	private TextField getLocalizeNameTextField(Binder<Movie> movieBinder) {
		TextField localizedNameTextField = new TextField("Name");
		movieBinder.forField(localizedNameTextField).withValidator(str -> str.length() > 0, "Movie name can't be emty")
				.bind(Movie::getLocalizedName, Movie::setLocalizedName);
		localizedNameTextField.setSizeFull();
		localizedNameTextField.setValue(editedMovie.getLocalizedName());
		return localizedNameTextField;
	}

	@Override
	public void showWarning(String message) {
		Notification.show(message, Notification.Type.WARNING_MESSAGE);
	}

	@Override
	public void showError(String message) {
		Notification.show(message, Notification.Type.ERROR_MESSAGE);
	}

	private void showNotification(String message) {
		Notification notification = new Notification(message, Notification.Type.HUMANIZED_MESSAGE);
		notification.show(Page.getCurrent());
	}

	@Override
	public void reload() {
		Page.getCurrent().reload();
	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		closeTab();
		event.navigate();
	}

	private void closeTab() {
		if ( this.tabSheet != null ) {
			this.tabSheet.removeAllComponents();
		}

	}

	private class ImageUploader implements Receiver, SucceededListener {

		private static final long serialVersionUID = -913452814113180117L;
		private File file;
		private Image image;

		public ImageUploader(Image image) {
			this.image = image;
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			FileOutputStream fos = null;
			try {
				file = new File(POSTER_PATH + filename);
				fos = new FileOutputStream(file);
				editedMovie.setPosterPath(POSTER_PATH + filename);
			} catch (final java.io.FileNotFoundException e) {
				showError("Could not open file");
				return null;
			}
			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			System.out.println(event.getFilename());
			image.setVisible(true);
			image.setSource(new FileResource(file));
			image.setAlternateText("POSTER");
			image.setWidth(POSTER_IMAGE_WIDTH, Unit.PIXELS);
			image.setHeight(POSTER_IMAGE_HEIGHT, Unit.PIXELS);
		}

	}
}
