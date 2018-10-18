package com.rustedbrain.study.course.view.cinema;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView(name = VaadinUI.CITY_VIEW)
public class CityViewImpl extends VerticalLayout implements CityView {

	private static final long serialVersionUID = -3911081956034709170L;

	public static final String CITY_ATTRIBUTE = "cityName";

	@Autowired
	public CityViewImpl(MenuComponent menuComponentView) {
		addComponentsAndExpand(menuComponentView);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// Long cityId = (Long) VaadinSession.getCurrent().getAttribute(CityView.CITY_ID_ATTRIBUTE);
		// City city = cinemaService.getCityByName(cityId);
		// if (city != null) {
		// addComponentsAndExpand(createCityPanel(city));
		// } else {
		// Notification.show("City is not selected");
		// }
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
