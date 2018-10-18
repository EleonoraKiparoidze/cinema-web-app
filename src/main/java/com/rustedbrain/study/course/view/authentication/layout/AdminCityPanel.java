package com.rustedbrain.study.course.view.authentication.layout;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdminCityPanel extends Panel {

	private static final long serialVersionUID = 188371442011236419L;
	protected List<City> cities;
	protected VerticalLayout layout = new VerticalLayout();
	private List<ProfileView.ViewListener> listeners;
	private Grid<City> grid = new Grid<>();

	public AdminCityPanel(List<ProfileView.ViewListener> listeners, List<City> cities) {
		this.listeners = listeners;
		this.cities = cities;
		this.layout.addComponent(new Panel(showCitySelectionPanel(cities)));
		setContent(this.layout);
	}

	private Layout showCitySelectionPanel(List<City> cities) {
		VerticalLayout mainLayout = new VerticalLayout();
		HorizontalLayout gridFormLayout = getGridSaveDeleteFormLayout(cities);
		HorizontalLayout filterLayout = getFilterLayout();

		Button addCityButton = new Button("Add new city");
		addCityButton.addClickListener(clickEvent -> {
			grid.deselectAll();
			filterLayout.addComponent(addNewCityButtonClick());
		});

		HorizontalLayout toolbar = new HorizontalLayout(filterLayout, addCityButton);
		mainLayout.addComponents(toolbar, gridFormLayout);
		return mainLayout;
	}

	private HorizontalLayout getFilterLayout() {
		TextField filterTextField = new TextField();
		filterTextField.setPlaceholder("Filter by name...");
		filterTextField.setValueChangeMode(ValueChangeMode.EAGER);
		filterTextField.addValueChangeListener(event -> getFilteredByNameCities(event.getValue()));

		Button clearFilterTextButton = new Button(VaadinIcons.CLOSE);
		clearFilterTextButton.setDescription("clear the current filter");
		clearFilterTextButton.addClickListener(clickEvent -> filterTextField.clear());

		HorizontalLayout filterLayout = new HorizontalLayout(filterTextField, clearFilterTextButton);
		filterLayout.setSpacing(false);
		return filterLayout;
	}

	private HorizontalLayout getGridSaveDeleteFormLayout(List<City> cities) {
		grid.setItems(cities);
		grid.addColumn(City::getName).setCaption("Name");
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.setSizeFull();
		SaveDeleteForm saveDeleteForm = new SaveDeleteForm();

		grid.addSelectionListener(selectionEvent -> {
			if ( grid.getSelectionModel().getFirstSelectedItem().isPresent() ) {
				saveDeleteForm.setSelectedCity(selectionEvent.getFirstSelectedItem().get());
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

	private PopupView addNewCityButtonClick() {
		VerticalLayout createPopupContent = new VerticalLayout();
		TextField cityNameTextField = new TextField("City name");
		createPopupContent.addComponent(cityNameTextField);
		createPopupContent.addComponent(new Button("Create", (Button.ClickListener) event -> {
			listeners.forEach(cityViewListener -> {
				cityViewListener.getCityEditPresenter().buttonAddNewCityClicked(cityNameTextField.getValue());
				showNotification("City create! City name: " + cityNameTextField.getValue());
				cityViewListener.reload();
			});
		}));

		PopupView createPopup = new PopupView(null, createPopupContent);
		createPopup.setSizeUndefined();
		createPopup.setPopupVisible(true);
		return createPopup;
	}

	private void showNotification(String message) {
		Notification notification = new Notification(message, Notification.Type.HUMANIZED_MESSAGE);
		notification.show(Page.getCurrent());
	}

	private void getFilteredByNameCities(String filterText) {
		if ( StringUtils.isEmpty(filterText) ) {
			grid.setItems(cities);
		} else {
			List<City> filteredCities =
					cities.stream().filter(city -> city.getName().contains(filterText)).collect(Collectors.toList());
			grid.setItems(filteredCities);
		}
	}

	private class SaveDeleteForm extends FormLayout {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7461847460285108909L;
		private City selectedCity;
		private TextField cityNameTextField;
		private Button saveButton;
		private Button deleteButton;

		private SaveDeleteForm() {
			cityNameTextField = new TextField("City Name");
			saveButton = new Button("Save");
			deleteButton = new Button("Delete");
			saveButton.addStyleName("friendly");
			deleteButton.addStyleName("danger");
			setSizeUndefined();

			HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);
			addComponents(cityNameTextField, buttons);

			deleteButton.addClickListener(clickEvent -> this.deleteCity(this.selectedCity));
			saveButton.addClickListener(clickEvent -> this.renameCity(this.selectedCity, cityNameTextField.getValue()));
		}

		private void deleteCity(City selectedCity) {
			listeners.forEach(listener -> listener.getCityEditPresenter().buttonDeleteCityClicked(selectedCity));
			this.setVisible(false);
			cities.remove(selectedCity);
			grid.setItems(cities);
		}

		private void renameCity(City selectedCity, String newCityName) {
			listeners.forEach(listener -> {
				listener.getCityEditPresenter().buttonSaveCityClicked(selectedCity, newCityName);
				listener.reload();
			});
		}

		void setSelectedCity(City selectedCity) {
			this.selectedCity = selectedCity;
			cityNameTextField.setValue(selectedCity.getName());

		}
	}
}
