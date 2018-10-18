package com.rustedbrain.study.course.view.authentication.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdminCinemaHallPanel extends Panel {

	private static final long serialVersionUID = 3029455009479340536L;
	protected VerticalLayout layout = new VerticalLayout();
	private List<ProfileView.ViewListener> listeners;
	private List<City> cities = new ArrayList<>();
	private List<CinemaHall> cinemaHalls = new ArrayList<>();
	private Grid<CinemaHall> grid = new Grid<>();
	private List<Cinema> cinemas = new ArrayList<>();

	public AdminCinemaHallPanel(List<ProfileView.ViewListener> listeners, List<City> cities) {
		this.listeners = listeners;
		this.cities = cities;
		this.cities.forEach(city -> cinemas.addAll(city.getCinemas()));
		this.cities.forEach(city -> city.getCinemas().forEach(cinema -> cinemaHalls.addAll(cinema.getCinemaHalls())));
		this.layout.addComponent(new Panel(showCinemaHallSelectionPanel(cinemaHalls)));
		setContent(this.layout);
	}

	private Layout showCinemaHallSelectionPanel(List<CinemaHall> cinemaHalls) {
		VerticalLayout mainLayout = new VerticalLayout();
		HorizontalLayout filterLayout = getFilterLayout();
		Button addNewCinemaButton = new Button("Add new cinema hall");

		addNewCinemaButton.addClickListener(clickEvent -> {
			grid.deselectAll();
			filterLayout.addComponent(addNewCinemaHallButtonClick());
		});

		HorizontalLayout toolbar = new HorizontalLayout(filterLayout, addNewCinemaButton);
		HorizontalLayout gridFormLayout = getGridSaveDeleteFormLayout(cinemaHalls);
		mainLayout.addComponents(toolbar, gridFormLayout);
		return mainLayout;
	}

	private HorizontalLayout getGridSaveDeleteFormLayout(List<CinemaHall> cinemaHalls) {
		grid.setItems(cinemaHalls);
		grid.addColumn(CinemaHall::getName).setCaption("Name");
		grid.addColumn(cinemaHall -> cinemaHall.getCinema().getName()).setCaption("Cinema");
		grid.addColumn(cinemaHall -> cinemaHall.getCinema().getCity().getName()).setCaption("City");
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.setSizeFull();
		SaveDeleteForm saveDeleteForm = new SaveDeleteForm();

		grid.addSelectionListener(selectionEvent -> {
			if ( grid.getSelectionModel().getFirstSelectedItem().isPresent() ) {
				saveDeleteForm.setSelectedCinemaHall(selectionEvent.getFirstSelectedItem().get());
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
		filterTextField.addValueChangeListener(event -> getFilteredByCinemaHallName(event.getValue()));

		Button clearFilterTextButton = new Button(VaadinIcons.CLOSE);
		clearFilterTextButton.setDescription("clear the current filter");
		clearFilterTextButton.addClickListener(clickEvent -> filterTextField.clear());

		HorizontalLayout filterLayout = new HorizontalLayout(filterTextField, clearFilterTextButton);
		filterLayout.setSpacing(false);
		return filterLayout;
	}

	private PopupView addNewCinemaHallButtonClick() {
		VerticalLayout createPopupContent = new VerticalLayout();

		TextField cinemaHallName = new TextField("Cinema Hall name");
		ComboBox<City> cityComboBox = new ComboBox<>("City");
		ComboBox<Cinema> cinemaComboBox = new ComboBox<>("Cinema");

		cityComboBox.setEmptySelectionAllowed(false);
		cityComboBox.setItems(cities);
		cityComboBox.setItemCaptionGenerator(City::getName);
		cityComboBox.setSelectedItem(cities.get(0));
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

		createPopupContent.addComponents(cinemaHallName, cityComboBox, cinemaComboBox);

		createPopupContent.addComponent(new Button("Next", (Button.ClickListener) event -> {
			listeners.forEach(cinemaHallViewListener -> {
				if ( cityComboBox.getSelectedItem().isPresent() ) {
					if ( cinemaComboBox.getSelectedItem().isPresent() ) {
						if ( !cinemaHallName.getValue().isEmpty() ) {
							cinemaHallViewListener.getCinemaHallEditPresenter().buttonAddNewCinemaHallClicked(
									cinemaHallName.getValue(), cinemaComboBox.getSelectedItem());
						} else {
							Notification.show("Please enter name", "", Notification.Type.ERROR_MESSAGE);
						}
					} else {
						Notification.show("Please select cinema", "", Notification.Type.ERROR_MESSAGE);
					}
				} else {
					Notification.show("Please select City", "", Notification.Type.ERROR_MESSAGE);
				}
			});
		}));

		PopupView createPopup = new PopupView(null, createPopupContent);
		createPopup.setSizeUndefined();
		createPopup.setPopupVisible(true);
		return createPopup;
	}

	private void getFilteredByCinemaHallName(String filterText) {
		if ( StringUtils.isEmpty(filterText) ) {
			grid.setItems(cinemaHalls);
		} else {
			List<CinemaHall> filteredCinemaHalls = cinemaHalls.stream()
					.filter(cinemaHall -> cinemaHall.getName().contains(filterText)).collect(Collectors.toList());
			grid.setItems(filteredCinemaHalls);
		}
	}

	private class SaveDeleteForm extends FormLayout {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4480753390741702693L;
		private CinemaHall selectedCinemaHall;
		private TextField cinemaHallNameTextField;
		private ComboBox<Cinema> cinemaComboBox;
		private Button changeCinemaHallSeatsButton;
		private Button saveButton;
		private Button deleteButton;

		private SaveDeleteForm() {
			cinemaHallNameTextField = new TextField("Cinema hall name");
			cinemaComboBox = new ComboBox<>("Cinema");
			changeCinemaHallSeatsButton = new Button("Change hall seats");
			saveButton = new Button("Save");
			deleteButton = new Button("Delete");
			saveButton.addStyleName("friendly");
			deleteButton.addStyleName("danger");
			setSizeUndefined();

			HorizontalLayout saveDeleteButtonsLayout = new HorizontalLayout(saveButton, deleteButton);
			addComponents(cinemaHallNameTextField, cinemaComboBox, changeCinemaHallSeatsButton,
					saveDeleteButtonsLayout);

			deleteButton.addClickListener(clickEvent -> {
				this.deleteCinemaHall(this.selectedCinemaHall);
				Notification.show("Cinema hall deleted", "Cinema hall name: " + this.selectedCinemaHall.getName(),
						Notification.Type.HUMANIZED_MESSAGE);
			});
			saveButton.addClickListener(clickEvent -> {
				this.editCinemaHall(this.selectedCinemaHall, cinemaHallNameTextField.getValue(),
						cinemaComboBox.getSelectedItem().get());
				Notification.show("Cinema hall edit", "Cinema hall name: " + this.selectedCinemaHall.getName(),
						Notification.Type.HUMANIZED_MESSAGE);
			});
			changeCinemaHallSeatsButton.addClickListener(clickEvent -> {
				this.changeCinemaHallSeats(this.selectedCinemaHall);
			});
		}

		private void deleteCinemaHall(CinemaHall selectedCinemaHall) {
			listeners.forEach(listener -> listener.getCinemaHallEditPresenter()
					.buttonDeleteCinemaHallClicked(selectedCinemaHall.getId()));
			this.setVisible(false);
			cinemaHalls.remove(selectedCinemaHall);
			grid.setItems(cinemaHalls);
		}

		private void editCinemaHall(CinemaHall selectedCinemaHall, String newCinemaHallName, Cinema newCinema) {
			listeners.forEach(listener -> {
				listener.getCinemaHallEditPresenter().buttonSaveCinemaHallClicked(selectedCinemaHall, newCinemaHallName,
						newCinema);
				listener.reload();
			});
		}

		private void changeCinemaHallSeats(CinemaHall selectedCinemaHall) {
			listeners.forEach(listener -> listener.getCinemaHallEditPresenter()
					.buttonChangeCinemaHallSeatsClicked(selectedCinemaHall));
		}

		public void setSelectedCinemaHall(CinemaHall selectedCinemaHall) {
			this.selectedCinemaHall = selectedCinemaHall;
			cinemaHallNameTextField.setValue(selectedCinemaHall.getName());

			cinemaComboBox.setItems(cinemas);
			cinemaComboBox.setItemCaptionGenerator(Cinema::getName);
			cinemaComboBox.setEmptySelectionAllowed(false);
			cinemaComboBox.setSelectedItem(selectedCinemaHall.getCinema());
		}
	}
}
