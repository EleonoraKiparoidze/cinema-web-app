package com.rustedbrain.study.course.view.authentication.layout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Feature;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdminCinemaPanel extends Panel {

	private static final long serialVersionUID = 2082376201001091998L;
	protected VerticalLayout layout = new VerticalLayout();
	private List<ProfileView.ViewListener> listeners;
	private Set<Cinema> cinemas = new HashSet<>();
	private Set<Feature> features = new HashSet<>();
	private List<Manager> managers = new ArrayList<>();
	private List<City> cities = new ArrayList<>();
	private Grid<Cinema> grid = new Grid<>();
	private boolean isAdmin;

	public AdminCinemaPanel(List<ProfileView.ViewListener> listeners, List<City> cities, List<Manager> managers,
			Set<Feature> features, boolean isAdmin) {
		this.listeners = listeners;
		this.cities = cities;
		this.managers = managers;
		this.features = features;
		this.isAdmin = isAdmin;
		this.cities.forEach(city -> cinemas.addAll(city.getCinemas()));
		this.layout.addComponent(new Panel(showCinemaSelectionPanel(cinemas)));
		setContent(this.layout);
	}

	private Layout showCinemaSelectionPanel(Set<Cinema> cinemas) {
		VerticalLayout mainLayout = new VerticalLayout();
		HorizontalLayout gridFormLayout = getGridSaveDeleteFormLayout(cinemas);
		HorizontalLayout filterLayout = getFilterLayout();
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addComponent(filterLayout);
		if ( isAdmin ) {
			Button addNewCinemaButton = new Button("Add new cinema");
			addNewCinemaButton.addClickListener(clickEvent -> {
				grid.deselectAll();
				filterLayout.addComponent(addNewCinemaButtonClick());
			});
			toolbar.addComponent(addNewCinemaButton);
		}
		mainLayout.addComponents(toolbar, gridFormLayout);
		return mainLayout;
	}

	private HorizontalLayout getGridSaveDeleteFormLayout(Set<Cinema> cinemas) {
		grid.setItems(cinemas);
		grid.addColumn(Cinema::getName).setCaption("Name");
		grid.addColumn(cinema -> cinema.getCity().getName()).setCaption("City");
		grid.addColumn(Cinema::getLocation).setCaption("Location");
		grid.addColumn(cinema -> {
			StringBuilder featuresStringBuilder = new StringBuilder();
			for (Feature feature : cinema.getFeatures()) {
				featuresStringBuilder.append(feature.getName()).append("; ");
			}
			return featuresStringBuilder;
		}).setCaption("Feature");

		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.setSizeFull();
		SaveDeleteForm saveDeleteForm = new SaveDeleteForm();

		grid.addSelectionListener(selectionEvent -> {
			if ( grid.getSelectionModel().getFirstSelectedItem().isPresent() ) {
				saveDeleteForm.setSelectedCinema(selectionEvent.getFirstSelectedItem().get());
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

	private HorizontalLayout getFilterLayout() {
		TextField filterTextField = new TextField();
		filterTextField.setPlaceholder("Filter by cinema name...");
		filterTextField.setValueChangeMode(ValueChangeMode.EAGER);
		filterTextField.addValueChangeListener(event -> getFilteredByCinemaName(event.getValue()));

		Button clearFilterTextButton = new Button(VaadinIcons.CLOSE);
		clearFilterTextButton.setDescription("clear the current filter");
		clearFilterTextButton.addClickListener(clickEvent -> filterTextField.clear());

		HorizontalLayout filterLayout = new HorizontalLayout(filterTextField, clearFilterTextButton);
		filterLayout.setSpacing(false);
		return filterLayout;
	}

	private Component addNewCinemaButtonClick() {
		VerticalLayout createPopupContent = new VerticalLayout();

		TextField cinemaNameTextField = new TextField("Cinema name");
		TextField cinemaLocationTextField = new TextField("Location");
		ComboBox<City> cinemaCityComboBox = new ComboBox<>("City");
		ComboBox<Manager> managerComboBox = new ComboBox<>("Manager");

		cinemaCityComboBox.setEmptySelectionAllowed(false);
		cinemaCityComboBox.setItems(cities);
		cinemaCityComboBox.setItemCaptionGenerator(City::getName);
		cinemaCityComboBox.setSelectedItem(cities.get(0));

		managerComboBox.setEmptySelectionAllowed(false);
		managerComboBox.setItems(managers);
		managerComboBox.setItemCaptionGenerator(Manager::getLogin);
		managerComboBox.setSelectedItem(managers.get(0));

		createPopupContent.addComponents(cinemaNameTextField, cinemaCityComboBox, cinemaLocationTextField,
				managerComboBox);

		createPopupContent.addComponent(new Button("Create", (Button.ClickListener) event -> {
			listeners.forEach(cinemaViewListener -> {
				if ( cinemaCityComboBox.getSelectedItem().isPresent() ) {
					cinemaViewListener.getCinemaEditPresenter().buttonAddNewCinemaClicked(
							cinemaCityComboBox.getSelectedItem().get(), cinemaNameTextField.getValue(),
							cinemaLocationTextField.getValue(), managerComboBox.getValue());
					cinemaViewListener.reload();
				} else {
					Notification.show("Please select city", "", Notification.Type.ERROR_MESSAGE);
				}
			});
		}));

		PopupView createPopup = new PopupView(null, createPopupContent);
		createPopup.setSizeUndefined();
		createPopup.setPopupVisible(true);
		return createPopup;
	}

	private void getFilteredByCinemaName(String filterText) {
		if ( StringUtils.isEmpty(filterText) ) {
			grid.setItems(cinemas);
		} else {
			List<Cinema> filteredCinemas = cinemas.stream().filter(cinema -> cinema.getName().contains(filterText))
					.collect(Collectors.toList());
			grid.setItems(filteredCinemas);
		}
	}

	private class SaveDeleteForm extends FormLayout {

		private static final long serialVersionUID = 1039337242156285553L;
		private Cinema selectedCinema;
		private TextField cinemaNameTextField;
		private ComboBox<City> cinemaCityComboBox;
		private ComboBox<Manager> cinemaManagerComboBox;
		private TextField cinemaLocationTextField;
		private Button saveButton;
		private Button deleteButton;
		private Button featuresButton;

		private SaveDeleteForm() {
			cinemaNameTextField = new TextField("Cinema Name");
			cinemaCityComboBox = new ComboBox<>("City");
			cinemaManagerComboBox = new ComboBox<>("Manager");
			cinemaLocationTextField = new TextField("Location");
			saveButton = new Button("Save");
			deleteButton = new Button("Delete");
			featuresButton = new Button("Features");
			saveButton.addStyleName("friendly");
			deleteButton.addStyleName("danger");
			setSizeUndefined();

			HorizontalLayout saveDeleteButtons = new HorizontalLayout(saveButton, deleteButton);
			addComponents(cinemaNameTextField, cinemaCityComboBox, cinemaLocationTextField, cinemaManagerComboBox,
					featuresButton, saveDeleteButtons);

			deleteButton.addClickListener(clickEvent -> {
				this.deleteCinema(this.selectedCinema);
				Notification.show("Cinema deleted", "Cinema name: " + this.selectedCinema.getName(),
						Notification.Type.HUMANIZED_MESSAGE);
			});
			saveButton.addClickListener(clickEvent -> {
				this.editCinema(this.selectedCinema, cinemaNameTextField.getValue(),
						cinemaCityComboBox.getSelectedItem().get(), cinemaLocationTextField.getValue(),
						cinemaManagerComboBox.getSelectedItem().get());
				Notification.show("Cinema edit", "Cinema name: " + this.selectedCinema.getName(),
						Notification.Type.HUMANIZED_MESSAGE);
			});

			featuresButton.addClickListener(clickEvent -> {
				this.editFeaturesButtonClicked(this.selectedCinema, features);
			});
		}

		private void editFeaturesButtonClicked(Cinema selectedCinema, Set<Feature> features) {
			listeners.forEach(listener -> listener.buttonFeaturesClicked(selectedCinema, features));
		}

		private void deleteCinema(Cinema selectedCinema) {
			listeners.forEach(
					listener -> listener.getCinemaEditPresenter().buttonDeleteCinemaClicked(selectedCinema.getId()));
			this.setVisible(false);
			cinemas.remove(selectedCinema);
			grid.setItems(cinemas);
		}

		private void editCinema(Cinema selectedCinema, String newCinemaName, City newCinemaCity,
				String newCinemaLocation, Manager newCinemaManager) {
			listeners.forEach(listener -> {
				listener.getCinemaEditPresenter().buttonSaveCinemaClicked(selectedCinema, newCinemaName, newCinemaCity,
						newCinemaLocation, newCinemaManager);
				listener.reload();
			});
		}

		void setSelectedCinema(Cinema selectedCinema) {
			this.selectedCinema = selectedCinema;
			cinemaNameTextField.setValue(selectedCinema.getName());
			cinemaLocationTextField.setValue(selectedCinema.getLocation());

			cinemaCityComboBox.setItems(cities);
			cinemaCityComboBox.setItemCaptionGenerator(City::getName);
			cinemaCityComboBox.setEmptySelectionAllowed(false);
			cinemaCityComboBox.setSelectedItem(selectedCinema.getCity());

			cinemaManagerComboBox.setItems(managers);
			cinemaManagerComboBox.setItemCaptionGenerator(Manager::getLogin);
			cinemaManagerComboBox.setEmptySelectionAllowed(false);
			cinemaManagerComboBox.setSelectedItem(selectedCinema.getManager());
		}
	}
}
