package com.rustedbrain.study.course.view.authentication.layout;

import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.rustedbrain.study.course.view.components.CityComboBox;
import com.vaadin.ui.*;

import java.util.List;

public class ProfileEditTab extends Panel {

	private static final long serialVersionUID = 902659509025444578L;
	private List<City> cities;
	protected VerticalLayout layout;
	User currUser;
	List<ProfileView.ViewListener> listeners;

	public ProfileEditTab(List<ProfileView.ViewListener> listeners, User currUser, List<City> cities) {
		this.listeners = listeners;
		this.currUser = currUser;
		this.cities = cities;
		setContent(new Panel(createUserInfoEditLayout()));
	}

	VerticalLayout createUserInfoEditLayout() {
		this.layout = new VerticalLayout();

		VerticalLayout currUserLayout = new VerticalLayout();
		TextField loginTextField = new TextField("New login", currUser.getLogin());
		currUserLayout.addComponent(getPopupLayout(loginTextField,
				(Button.ClickListener) event -> listeners.forEach(
						listener -> listener.buttonChangeLoginClicked(currUser.getId(), loginTextField.getValue())),
				"login", currUser.getLogin(), layout));

		TextField nameTextField = new TextField("New name", currUser.getName() != null ? currUser.getName() : "N/A");
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

		TextField mailTextField = new TextField("New mail", currUser.getEmail() != null ? currUser.getEmail() : "N/A");
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

		Label blockDateLabel = new Label(
				"Blocked until: " + (currUser.getBlockPeriod() != null ? currUser.getBlockPeriod().toString() : "N/A"));
		currUserLayout.addComponent(blockDateLabel);

		Label blockDescrLabel = new Label("Block description: "
				+ (currUser.getBlockDescription() != null ? currUser.getBlockDescription() : "N/A"));
		currUserLayout.addComponent(blockDescrLabel);

		layout.addComponent(new Panel(currUserLayout));
		return layout;
	}

	public void setSelectedUser(User selectedUser) {
		this.currUser = selectedUser;
		setContent(createUserInfoEditLayout());
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
