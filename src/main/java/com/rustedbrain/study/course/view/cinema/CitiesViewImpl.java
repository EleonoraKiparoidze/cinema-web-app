package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UIScope
@SpringView(name = VaadinUI.CITIES_VIEW)
public class CitiesViewImpl extends VerticalLayout implements CitiesView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5657069621511984178L;
	private static final int MAX_PAGES_TO_VIEW_LEFT = 2;
	private static final int MAX_PAGES_TO_VIEW_RIGHT = 2;

	private Collection<CitiesView.CitiesViewListener> citiesViewListeners = new ArrayList<>();
	private VerticalLayout citiesPagesLayout;

	@Autowired
	public CitiesViewImpl(MenuComponent menuComponent) {
		addComponentsAndExpand(menuComponent);
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(createCitiesPagesPanel());
		layout.addComponent(createCitiesPanel());
		addComponentsAndExpand(layout);
	}

	private Component createCitiesPagesPanel() {
		citiesPagesLayout = new VerticalLayout();
		return new Panel(citiesPagesLayout);
	}

	private Component createCitiesPanel() {
		Panel panel = new Panel();
		return panel;
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
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
	public void fillCitiesPanel(List<City> cities) {

	}

	@Override
	@Autowired
	public void addCitiesViewListener(CitiesViewListener listener) {
		listener.setView(this);
		citiesViewListeners.add(listener);
	}

	@Override
	public void setCurrentCitiesPageNumber(int currentCitiesPageNumber, int totalPages) {
		citiesPagesLayout.removeAllComponents();
		for (int i = 1; (i <= MAX_PAGES_TO_VIEW_LEFT) && (currentCitiesPageNumber - i > 0); i++) {
			Button button = new Button(Integer.toString(currentCitiesPageNumber - i));
			button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			citiesPagesLayout.addComponent(button);
		}
		Button currentPageButton = new Button(Integer.toString(currentCitiesPageNumber));
		currentPageButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		citiesPagesLayout.addComponent(currentPageButton);

		for (int i = 1; (i <= MAX_PAGES_TO_VIEW_RIGHT) && (currentCitiesPageNumber + i <= totalPages); i++) {
			Button button = new Button(Integer.toString(currentCitiesPageNumber + i));
			button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			citiesPagesLayout.addComponent(button);
		}
	}
}
