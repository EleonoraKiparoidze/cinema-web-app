package com.rustedbrain.study.course.view.cinema;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.dto.UserInfo;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;

public interface ProfileInfoView extends ApplicationView {

	@Autowired
	void addCinemaViewListener(Listener listener);

	void showNotAuthError();

	public interface Listener {

		void setView(ProfileInfoView view);

		void entered(ViewChangeListener.ViewChangeEvent event);

		void buttonAuthorizeClicked();

		void buttonRegisterClicked();

		void buttonChangeLoginClicked(long id, String login);

		void buttonChangeNameClicked(long id, String name);

		void buttonChangeSurnameClicked(long id, String surname);

		void buttonChangeMailClicked(long id, String mail);

		void buttonChangeCityClicked(long id, long cityId);

		void buttonChangeBirthdayClicked(long id, LocalDate birthday);
	}

	void showUserInfo(UserInfo userInfo, boolean ableToEdit, List<City> cities);
}
