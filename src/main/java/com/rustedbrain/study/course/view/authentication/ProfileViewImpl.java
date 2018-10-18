package com.rustedbrain.study.course.view.authentication;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.authorization.ChangeRequest;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Feature;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.authentication.layout.AdminCinemaHallPanel;
import com.rustedbrain.study.course.view.authentication.layout.AdminCinemaPanel;
import com.rustedbrain.study.course.view.authentication.layout.AdminCityPanel;
import com.rustedbrain.study.course.view.authentication.layout.AdminFilmScreeningPanel;
import com.rustedbrain.study.course.view.authentication.layout.AdminMessageTab;
import com.rustedbrain.study.course.view.authentication.layout.AdminMoviePanel;
import com.rustedbrain.study.course.view.authentication.layout.AdminProfileEditTab;
import com.rustedbrain.study.course.view.authentication.layout.AdminStatisticPanel;
import com.rustedbrain.study.course.view.authentication.layout.MessageTab;
import com.rustedbrain.study.course.view.authentication.layout.ProfileEditTab;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = VaadinUI.PROFILE_VIEW)
public class ProfileViewImpl extends VerticalLayout implements ProfileView {

	private static final long serialVersionUID = 1036618039435390691L;

	private List<ProfileView.ViewListener> listeners = new ArrayList<>();

	private TabSheet tabSheet;
	private ProfileEditTab profileEditTab;
	private MessageTab messageTab;
	private AdminMessageTab adminMessageTab;
	private TabSheet adminLayout;
	private Panel statisticsLayout;
	private Window userBlockWindow;
	private Window filmScreeningEventsWindow;
	private Window featuresWindow;
	private AdminCityPanel administrationCityPanel;
	private AdminCinemaPanel administrationCinemaPanel;
	private AdminCinemaHallPanel administrationCinemaHallPanel;
	private AdminMoviePanel administrationMoviePanel;
	private AdminFilmScreeningPanel administrationFilmScreeningPanel;

	@Autowired
	public ProfileViewImpl(AuthenticationService authenticationService) {
		addComponentsAndExpand(new Panel(new MenuComponent(authenticationService)));
		tabSheet = new TabSheet();
		tabSheet.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
		addComponentsAndExpand(tabSheet);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		listeners.forEach(viewListener -> viewListener.entered(event));
	}

	@Override
	@Autowired
	public void addListener(ProfileView.ViewListener listener) {
		listener.setView(this);
		listeners.add(listener);
	}

	@Override
	public void showWarning(String message) {
		Notification.show(message, Notification.Type.WARNING_MESSAGE);
	}

	@Override
	public void showError(String message) {
		Notification.show(message, Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void reload() {
		Page.getCurrent().reload();
	}

	@Override
	public void addProfileAdminEditTab(User user, List<User> users, List<City> cities) {
		if ( profileEditTab != null ) {
			Component oldProfileEditLayout = this.profileEditTab;
			this.profileEditTab = new AdminProfileEditTab(listeners, user, users, cities);
			tabSheet.replaceComponent(oldProfileEditLayout, profileEditTab);
		} else {
			this.profileEditTab = new AdminProfileEditTab(listeners, user, users, cities);
			tabSheet.addTab(profileEditTab, "Edit");
		}
	}

	@Override
	public void addMessageTab(User currUser, List<ChangeRequest> changeRequests) {
		if ( messageTab != null ) {
			Component oldMessageLayout = this.messageTab;
			this.messageTab = new MessageTab(currUser, changeRequests);
			tabSheet.replaceComponent(oldMessageLayout, messageTab);
		} else {
			this.messageTab = new MessageTab(currUser, changeRequests);
			tabSheet.addTab(messageTab, "Message");
		}
	}

	@Override
	public void addMessageAdminTab(List<User> users, List<ChangeRequest> changeRequests) {
		if ( adminMessageTab != null ) {
			Component oldMessageLayout = this.adminMessageTab;
			this.adminMessageTab = new AdminMessageTab(listeners, changeRequests, users);
			tabSheet.replaceComponent(oldMessageLayout, adminMessageTab);
		} else {
			this.adminMessageTab = new AdminMessageTab(listeners, changeRequests, users);
			tabSheet.addTab(adminMessageTab, "Message");
		}
	}

	@Override
	public void closeUserBlockWindow() {
		if ( this.userBlockWindow != null ) {
			this.userBlockWindow.close();
		}
	}

	@Override
	public void closeFilmScreeningEventsWindow() {
		if ( this.filmScreeningEventsWindow != null ) {
			this.filmScreeningEventsWindow.close();
		}
	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		closeUserBlockWindow();
		event.navigate();
	}

	@Override
	public void showUserBlockWindow(long id, String login, UserRole userRole) {
		closeUserBlockWindow();
		userBlockWindow = new Window("Block User");
		userBlockWindow.addCloseListener((Window.CloseListener) e -> userBlockWindow = null);
		userBlockWindow.setSizeUndefined();
		userBlockWindow.setModal(true);
		userBlockWindow.setResizable(false);
		userBlockWindow.setDraggable(false);

		Label warnLabel = new Label("If you want to block user \"" + login + "\", please specify date and description");
		DateTimeField blockDateTimeField = new DateTimeField("Block date", LocalDateTime.now());
		TextArea blockDescrTextField = new TextArea("Reason of blocking");
		blockDescrTextField.setSizeFull();
		Button buttonBlock =
				new Button("Block", (Button.ClickListener) event -> listeners.forEach(viewListener -> viewListener
						.buttonBlockSubmitClicked(id, blockDateTimeField.getValue(), blockDescrTextField.getValue())));
		userBlockWindow.setContent(new VerticalLayout(warnLabel, blockDateTimeField, blockDescrTextField, buttonBlock));

		UI.getCurrent().addWindow(userBlockWindow);
	}

	@Override
	public void addAdministrationTab(User currUser, List<City> cities, List<Movie> movies, List<Manager> managers,
			Set<Feature> features, boolean isAdmin) {
		if ( adminLayout != null ) {
			TabSheet oldAdminLayout = this.adminLayout;
			this.adminLayout = createAdminTab(cities, movies, managers, features, isAdmin);
			tabSheet.replaceComponent(oldAdminLayout, this.adminLayout);
		} else {
			this.adminLayout = createAdminTab(cities, movies, managers, features, isAdmin);
			tabSheet.addTab(adminLayout, "Administration");
		}
	}

	private TabSheet createAdminTab(List<City> cities, List<Movie> movies, List<Manager> managers,
			Set<Feature> features, boolean isAdmin) {
		TabSheet tabSheet = new TabSheet();
		if ( isAdmin ) {
			administrationCityPanel = new AdminCityPanel(listeners, cities);
			tabSheet.addTab(administrationCityPanel, "City");
		}
		administrationCinemaPanel = new AdminCinemaPanel(listeners, cities, managers, features, isAdmin);
		tabSheet.addTab(administrationCinemaPanel, "Cinema");
		administrationCinemaHallPanel = new AdminCinemaHallPanel(listeners, cities);
		tabSheet.addTab(administrationCinemaHallPanel, "Cinema Hall");
		if ( isAdmin ) {
			administrationMoviePanel = new AdminMoviePanel(listeners, movies);
			tabSheet.addTab(administrationMoviePanel, "Movie");
		}
		administrationFilmScreeningPanel = new AdminFilmScreeningPanel(listeners, cities, movies);
		tabSheet.addTab(administrationFilmScreeningPanel, "Film Screening");
		return tabSheet;
	}

	@Override
	public void addStatisticsTab(List<City> cities) {
		if ( statisticsLayout != null ) {
			Panel oldStatisticsLayout = this.statisticsLayout;
			this.statisticsLayout = createStatisticsTab(cities);
			tabSheet.replaceComponent(oldStatisticsLayout, this.statisticsLayout);
		} else {
			this.statisticsLayout = createStatisticsTab(cities);
			tabSheet.addTab(statisticsLayout, "Statistics");
		}
	}

	@Override
	public Panel createStatisticsTab(List<City> cities) {
		return new AdminStatisticPanel(listeners, cities);
	}

	@Override
	public void setAdminEditUserSelected(User currUser) {
		profileEditTab.setSelectedUser(currUser);
	}

	@Override
	public void addProfileEditTab(User currUser, List<City> cities) {
		if ( profileEditTab != null ) {
			Component oldProfileEditLayout = this.profileEditTab;
			this.profileEditTab = new ProfileEditTab(listeners, currUser, cities);
			tabSheet.replaceComponent(oldProfileEditLayout, profileEditTab);
		} else {
			this.profileEditTab = new ProfileEditTab(listeners, currUser, cities);
			tabSheet.addTab(profileEditTab, "Edit");
		}
	}

	@Override
	public void showFilmScreeningEventsWindow(FilmScreening selectedFilmScreening) {
		closeFilmScreeningEventsWindow();
		filmScreeningEventsWindow = new Window("Film screening events");
		filmScreeningEventsWindow.addCloseListener((Window.CloseListener) e -> filmScreeningEventsWindow = null);
		filmScreeningEventsWindow.setWidth("800px");
		filmScreeningEventsWindow.setModal(true);
		filmScreeningEventsWindow.setDraggable(false);

		List<FilmScreeningEvent> filmScreeningEvents = selectedFilmScreening.getFilmScreeningEvents();

		Grid<FilmScreeningEvent> grid = new Grid<>();
		grid.setItems(filmScreeningEvents);
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.setSizeFull();

		grid.addColumn(FilmScreeningEvent::getDate).setCaption("Date");
		grid.addColumn(FilmScreeningEvent::getTime).setCaption("Time");
		grid.addColumn(filmScreeningEvent -> filmScreeningEvent.getCinemaHall().getCinema().getName())
				.setCaption("Cinema");
		grid.addColumn(filmScreeningEvent -> filmScreeningEvent.getCinemaHall().getName()).setCaption("Cinema hall");
		grid.addColumn(filmScreeningEvent -> "Delete", new ButtonRenderer<FilmScreeningEvent>(clickEvent -> {
			filmScreeningEvents.remove(clickEvent.getItem());
			grid.setItems(filmScreeningEvents);
			listeners.forEach(listener -> listener.getFilmScreeningEditPresenter()
					.buttonDeleteFilmScreeningEventClicked(clickEvent.getItem().getId()));
		}));

		VerticalLayout verticalLayout = new VerticalLayout();

		Button addFilmScreeningEvent = new Button("Add");
		addFilmScreeningEvent.addClickListener(e -> {
			verticalLayout.addComponent(
					addNewFilmScreeningEventButtonClick(selectedFilmScreening, grid, filmScreeningEvents));
		});

		verticalLayout.addComponents(grid, addFilmScreeningEvent);
		filmScreeningEventsWindow.setContent(verticalLayout);
		UI.getCurrent().addWindow(filmScreeningEventsWindow);
	}

	private PopupView addNewFilmScreeningEventButtonClick(FilmScreening filmScreening, Grid<FilmScreeningEvent> grid,
			List<FilmScreeningEvent> filmScreeningEvents) {
		VerticalLayout createPopupContent = new VerticalLayout();

		Set<CinemaHall> cinemaHalls = filmScreening.getCinema().getCinemaHalls();
		ComboBox<CinemaHall> cinemaHallComboBox = new ComboBox<>("Cinema hall");
		cinemaHallComboBox.setEmptySelectionAllowed(false);
		cinemaHallComboBox.setItems(cinemaHalls);
		cinemaHallComboBox.setItemCaptionGenerator(CinemaHall::getName);
		cinemaHallComboBox.setSelectedItem(cinemaHalls.iterator().next());
		cinemaHallComboBox.setSizeFull();

		DateTimeField filmScreeningEventDateTimeField = new DateTimeField("Date time");
		createPopupContent.addComponents(cinemaHallComboBox, filmScreeningEventDateTimeField);

		createPopupContent.addComponent(new Button("Add", (Button.ClickListener) event -> {
			listeners.forEach(listener -> {
				if ( cinemaHallComboBox.getSelectedItem().isPresent() ) {
					LocalDate dateEvent =
							filmScreeningEventDateTimeField.getValue().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalTime timeEvent =
							filmScreeningEventDateTimeField.getValue().atZone(ZoneId.systemDefault()).toLocalTime();
					ZonedDateTime zdtEnd =
							ZonedDateTime.ofInstant(filmScreening.getEndDate().toInstant(), ZoneId.systemDefault());
					ZonedDateTime zdtStart =
							ZonedDateTime.ofInstant(filmScreening.getStartDate().toInstant(), ZoneId.systemDefault());
					if ( filmScreeningEventDateTimeField.getValue().isBefore(zdtEnd.toLocalDateTime())
							&& filmScreeningEventDateTimeField.getValue().isAfter(zdtStart.toLocalDateTime()) ) {
						listener.getFilmScreeningEditPresenter().buttonAddNewFilmScreeningEventClicked(filmScreening,
								cinemaHallComboBox.getSelectedItem().get(), Date.valueOf(dateEvent),
								Time.valueOf(timeEvent));
						filmScreeningEvents.add(new FilmScreeningEvent(cinemaHallComboBox.getSelectedItem().get(),
								Date.valueOf(dateEvent), Time.valueOf(timeEvent)));
						grid.setItems(filmScreeningEvents);
					} else {
						showError("Date and time must be after start date of film screening");
					}
				} else {
					showError("Please select cinema hall");
				}
			});
		}));

		PopupView popup = new PopupView(null, createPopupContent);
		popup.setSizeUndefined();
		popup.setPopupVisible(true);
		return popup;
	}

	@Override
	public void showFeaturesWindow(Cinema selectedCinema, Set<Feature> features) {
		closeFeaturesWindow();
		featuresWindow = new Window("Features");
		featuresWindow.addCloseListener((Window.CloseListener) e -> featuresWindow = null);
		featuresWindow.setWidth("800px");
		featuresWindow.setModal(true);
		featuresWindow.setDraggable(false);

		Grid<Feature> grid = new Grid<>();
		grid.setItems(features);
		grid.setSelectionMode(Grid.SelectionMode.MULTI);
		grid.setSizeFull();
		selectedCinema.getFeatures().forEach(feature -> grid.select(feature));

		grid.addColumn(Feature::getName).setCaption("Name");
		grid.addColumn(Feature::getFeatureDescription).setCaption("Description");

		VerticalLayout verticalLayout = new VerticalLayout();
		grid.addColumn(feature -> "Edit", new ButtonRenderer<Feature>(clickEvent -> {
			verticalLayout.addComponent(editFeatureButtonClick(grid, features, clickEvent.getItem()));
		}));

		Button saveButton = new Button("Save", (Button.ClickListener) event -> {
			listeners.forEach(listener -> listener.getCinemaEditPresenter()
					.buttonSaveCinemaFeaturesButtonClicked(selectedCinema, grid.getSelectedItems()));
			selectedCinema.setFeatures(grid.getSelectedItems());
			closeFeaturesWindow();
		});
		saveButton.addStyleName("friendly");

		Button addFeature = new Button("Add new feature");
		addFeature.addClickListener(e -> {
			verticalLayout.addComponent(addNewFeatureButtonClick(grid, features));
			reload();
		});

		verticalLayout.addComponents(new Panel(new VerticalLayout(grid, addFeature)), saveButton);
		featuresWindow.setContent(verticalLayout);
		UI.getCurrent().addWindow(featuresWindow);
	}

	private PopupView editFeatureButtonClick(Grid<Feature> grid, Set<Feature> features, Feature feature) {
		VerticalLayout createPopupContent = new VerticalLayout();

		TextField featureNameTextField = new TextField("New name");
		featureNameTextField.setValue(feature.getName().isEmpty() ? " " : feature.getName());
		TextArea featureDescriptionTextArea = new TextArea("New description");
		featureDescriptionTextArea
				.setValue(feature.getFeatureDescription() == null ? " " : feature.getFeatureDescription());
		createPopupContent.addComponents(featureNameTextField, featureDescriptionTextArea);

		createPopupContent.addComponent(new Button("Edit", (Button.ClickListener) event -> {
			listeners.forEach(listener -> {
				if ( !featureNameTextField.getValue().isEmpty() ) {
					listener.getCinemaEditPresenter().buttonEditFeatureClicked(feature.getId(),
							featureNameTextField.getValue(), featureDescriptionTextArea.getValue());
					reload();
				} else {
					showError("Please enter feature name");
				}
			});
		}));

		PopupView popup = new PopupView(null, createPopupContent);
		popup.setSizeUndefined();
		popup.setPopupVisible(true);
		return popup;
	}

	private PopupView addNewFeatureButtonClick(Grid<Feature> grid, Set<Feature> features) {
		VerticalLayout createPopupContent = new VerticalLayout();

		TextField featureNameTextField = new TextField("Name");
		TextArea featureDescriptionTextArea = new TextArea("Description");
		createPopupContent.addComponents(featureNameTextField, featureDescriptionTextArea);

		createPopupContent.addComponent(new Button("Add", (Button.ClickListener) event -> {
			listeners.forEach(listener -> {
				if ( !featureNameTextField.getValue().isEmpty() ) {
					listener.getCinemaEditPresenter().buttonAddNewFeatureClicked(featureNameTextField.getValue(),
							featureDescriptionTextArea.getValue());
					features.add(new Feature(featureNameTextField.getValue(), featureDescriptionTextArea.getValue()));
					grid.setItems(features);
				} else {
					showError("Please enter feature name");
				}
			});
		}));

		PopupView popup = new PopupView(null, createPopupContent);
		popup.setSizeUndefined();
		popup.setPopupVisible(true);
		return popup;
	}

	@Override
	public void closeFeaturesWindow() {
		if ( this.featuresWindow != null ) {
			this.featuresWindow.close();
		}
	}

}