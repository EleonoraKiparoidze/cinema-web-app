package com.rustedbrain.study.course.view.authentication.layout;

import java.util.List;

import com.rustedbrain.study.course.model.dto.UserInfo;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.cinema.ProfileInfoView;
import com.rustedbrain.study.course.view.components.CityComboBox;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UserInfoLayout extends Panel {

	private static final long serialVersionUID = 7988254996096858246L;
	private List<ProfileInfoView.Listener> listeners;
	private UserInfo userInfo;
	private List<City> cities;
	private VerticalLayout layout;
	private boolean ableToEdit;

	public UserInfoLayout(List<ProfileInfoView.Listener> listeners, UserInfo userInfo, boolean ableToEdit, List<City> cities) {
		this.listeners = listeners;
		this.userInfo = userInfo;
		this.cities = cities;
		this.ableToEdit = ableToEdit;
		setContent(new Panel(createUserInfoLayout()));
	}

	public final VerticalLayout createUserInfoLayout() {
		this.layout = new VerticalLayout();
		if ( !ableToEdit ) {
			layout.addComponent(new Label("Login: " + userInfo.getLogin()));
			layout.addComponent(new Label("Name: " + (userInfo.getName() != null ? userInfo.getName() : "N/A")));
			layout.addComponent(
					new Label("Surname: " + (userInfo.getSurname() != null ? userInfo.getSurname() : "N/A")));
			layout.addComponent(new Label("Role: " + userInfo.getRole().toString()));
			layout.addComponent(new Label("City: " + (userInfo.getCity() != null ? userInfo.getCity() : "N/A")));
			layout.addComponent(
					new Label("Birthday: " + (userInfo.getBirthday() != null ? userInfo.getBirthday() : "N/A")));
			layout.addComponent(new Label("Last login: " + userInfo.getLastAccessDate()));
			layout.addComponent(new Label("Profile created: " + userInfo.getRegistrationDate()));
			return layout;
		} else {
			User currUser = userInfo.getUser();
			VerticalLayout currUserLayout = new VerticalLayout();
			TextField loginTextField = new TextField("New login", currUser.getLogin());
			currUserLayout.addComponent(getPopupLayout(loginTextField,
					(Button.ClickListener) event -> listeners.forEach(
							listener -> listener.buttonChangeLoginClicked(currUser.getId(), loginTextField.getValue())),
					"login", currUser.getLogin(), layout));

			TextField nameTextField =
					new TextField("New name", currUser.getName() != null ? currUser.getName() : "N/A");
			currUserLayout.addComponent(getPopupLayout(nameTextField,
					(Button.ClickListener) event -> listeners.forEach(
							listener -> listener.buttonChangeNameClicked(currUser.getId(), nameTextField.getValue())),
					"name", currUser.getName(), layout));

			TextField newValueTextField =
					new TextField("New surname", currUser.getSurname() != null ? currUser.getSurname() : "N/A");
			currUserLayout.addComponent(getPopupLayout(newValueTextField,
					(Button.ClickListener) event -> listeners.forEach(listener -> listener
							.buttonChangeSurnameClicked(currUser.getId(), newValueTextField.getValue())),
					"surname", currUser.getSurname(), layout));

			TextField mailTextField =
					new TextField("New mail", currUser.getEmail() != null ? currUser.getEmail() : "N/A");
			currUserLayout.addComponent(getPopupLayout(mailTextField,
					(Button.ClickListener) event -> listeners.forEach(
							listener -> listener.buttonChangeMailClicked(currUser.getId(), mailTextField.getValue())),
					"mail", currUser.getEmail(), layout));

			CityComboBox cityComboBox = new CityComboBox(cities, "New city");
			currUserLayout.addComponent(getPopupLayout(cityComboBox,
					(Button.ClickListener) event -> listeners.forEach(listener -> listener
							.buttonChangeCityClicked(currUser.getId(), cityComboBox.getValue().getId())),
					"city", currUser.getCity() != null ? currUser.getCity().getName() : null, layout));

			DateField birthdayDateField = new DateField("New birthday");
			currUserLayout.addComponent(getPopupLayout(birthdayDateField,
					(Button.ClickListener) event -> listeners.forEach(listener -> listener
							.buttonChangeBirthdayClicked(currUser.getId(), birthdayDateField.getValue())),
					"birthday", currUser.getBirthday() != null ? currUser.getBirthday().toString() : null, layout));

			Label blockDateLabel = new Label("Blocked until: "
					+ (currUser.getBlockPeriod() != null ? currUser.getBlockPeriod().toString() : "N/A"));
			currUserLayout.addComponent(blockDateLabel);

			Label blockDescrLabel = new Label("Block description: "
					+ (currUser.getBlockDescription() != null ? currUser.getBlockDescription() : "N/A"));
			currUserLayout.addComponent(blockDescrLabel);

			layout.addComponent(new Panel(currUserLayout));
			return layout;
		}
	}

	public void setSelectedUser(UserInfo selectedUser) {
		this.userInfo = selectedUser;
		setContent(createUserInfoLayout());
	}

	private Layout getPopupLayout(AbstractComponent newValueTextField, Button.ClickListener bClickListener,
			String propertyKey, String propertyValue, Layout targetLayout) {
		VerticalLayout changeSurnamePopupContent = new VerticalLayout();
		changeSurnamePopupContent.addComponent(newValueTextField);
		changeSurnamePopupContent.addComponent(new Button("Change", bClickListener));
		return new HorizontalLayout(new Label(propertyKey + ": "),
				new PopupView(propertyValue != null ? propertyValue : "N/A", changeSurnamePopupContent));
	}
}
