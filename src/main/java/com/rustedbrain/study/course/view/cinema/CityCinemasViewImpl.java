package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.Feature;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringView(name = VaadinUI.CITY_CINEMAS_VIEW)
public class CityCinemasViewImpl extends VerticalLayout implements CityCinemasView {

	private static final long serialVersionUID = -7998168209329334788L;
	private List<CityCinemasViewListener> listeners = new ArrayList<>();
	private Panel cinemasPanel = new Panel();

	@Autowired
	public CityCinemasViewImpl(AuthenticationService authenticationService) {
		addComponentsAndExpand(new Panel(new MenuComponent(authenticationService)));
		addComponentsAndExpand(initCinemasPanel());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		listeners.forEach(listener -> listener.entered(event));
	}

	@Override
	public void showCinemasPanel(List<Cinema> cinemas) {
		Accordion cinemasAccordion = new Accordion();
		for (Cinema cinema : cinemas) {
			VerticalLayout cinemaInfoLayout = new VerticalLayout();

			cinemaInfoLayout.addComponent(new Label("Location: " + cinema.getLocation()));

			StringBuilder featuresStringBuilder = new StringBuilder("Features: ");
			for (Feature feature : cinema.getFeatures()) {
				featuresStringBuilder.append(feature.getName()).append("; ");
			}
			cinemaInfoLayout.addComponent(new Label(featuresStringBuilder.toString()));

			Button navigateButton =
					new Button("Show Cinema", clickEvent -> new PageNavigator().navigateToCinemaView(cinema.getId()));
			navigateButton.setSizeFull();
			cinemaInfoLayout.addComponent(navigateButton);
			cinemasAccordion.addTab(cinemaInfoLayout, cinema.getName());
		}
		cinemasPanel.setContent(cinemasAccordion);
	}

	private Component initCinemasPanel() {
		this.cinemasPanel = new Panel();
		return cinemasPanel;
	}

	@Override
	@Autowired
	public void addCityCinemasViewListener(CityCinemasViewListener listener) {
		listener.setView(this);
		this.listeners.add(listener);
	}

	@Override
	public void setCinemasPageCount(int totalPages) {

	}

	@Override
	public void setCurrentCinemasPage(int currentCinemasPage) {

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
}
