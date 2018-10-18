package com.rustedbrain.study.course.view.authentication;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public interface RegistrationView extends ApplicationView {

	void voidShowRegistrationPanel(List<City> cities);

	@Autowired
	void addListener(ViewListener viewListener);

	interface ViewListener {
		void entered(ViewChangeListener.ViewChangeEvent event);

		void buttonRegisterClicked(String login, String password, String name, String surname, City city,
				LocalDate birthday, String mail);

		void setView(RegistrationView view);
	}
}
