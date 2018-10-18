package com.rustedbrain.study.course.view.authentication;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.components.CityComboBox;
import com.vaadin.data.Result;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringView(name = VaadinUI.REGISTRATION_VIEW)
public class RegistrationViewImpl extends VerticalLayout implements RegistrationView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9020052590247572371L;
	private List<RegistrationView.ViewListener> listeners = new ArrayList<>();

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		listeners.forEach(listener -> listener.entered(event));
	}

	@Override
	public void voidShowRegistrationPanel(List<City> cities) {
		Panel panel = new Panel("Registration");
		panel.setSizeUndefined();
		addComponent(panel);

		FormLayout content = new FormLayout();
		TextField loginTextField = new TextField("Login");
		content.addComponent(loginTextField);

		TextField passwordTextField = new PasswordField("Password");
		content.addComponent(passwordTextField);

		TextField nameTextField = new TextField("Name");
		content.addComponent(nameTextField);

		TextField surnameTextField = new TextField("Surname");
		content.addComponent(surnameTextField);

		ComboBox<City> cityComboBox = new CityComboBox(cities, "City");
		content.addComponent(cityComboBox);

		DateField birthdayDateField = new DateField("Birthday") {
			/**
			 * 
			 */
			private static final long serialVersionUID = -542755325515871347L;

			@Override
			protected Result<LocalDate> handleUnparsableDateString(String dateString) {
				try {
					// try to parse with alternative format
					LocalDate parsedAtServer = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
					return Result.ok(parsedAtServer);
				} catch (DateTimeParseException e) {
					return Result.error("Bad date format");
				}
			}
		};
		content.addComponent(birthdayDateField);

		TextField mailTextField = new TextField("Mail");
		content.addComponent(mailTextField);

		Button buttonRegister = new Button("Register",
				(Button.ClickListener) event -> listeners
						.forEach(viewListener -> viewListener.buttonRegisterClicked(loginTextField.getValue(),
								passwordTextField.getValue(), nameTextField.getValue(), surnameTextField.getValue(),
								cityComboBox.getValue(), birthdayDateField.getValue(), mailTextField.getValue())));

		buttonRegister.setSizeFull();
		content.addComponent(buttonRegister);

//        Button register = new Button("Register");
//        register.addClickListener(clickEvent -> {
//            if (authorizationUserService.isUniqueUser(loginTextField.getValue(), mailTextField.getValue())) {
//                try {
//                    String login = getNonEmptyFieldValue(loginTextField, "Login cannot be empty");
//                    String password = getNonEmptyFieldValue(passwordTextField, "Password cannot be empty");
//                    String email = getNonEmptyFieldValue(mailTextField, "Mail cannot be empty");
//                    String name = nameTextField.getValue();
//                    String surname = surnameTextField.getValue();
//                    City city = cityComboBox.getValue();
//                    LocalDate birthday = birthdayDateField.getValue();
//
//                    Member member = new Member();
//                    member.setName(name);
//                    member.setSurname(surname);
//                    member.setEmail(email);
//                    member.setLogin(login);
//                    member.setPassword(password);
//                    member.setCity(city);
//                    member.setBirthday(Date.valueOf(birthday));
//
//                    authorizationUserService.registerMember(member);
//                    Page.getCurrent().setUriFragment("!" + VaadinUI.NAVIGATION_MENU_VIEW);
//                } catch (IllegalArgumentException ex) {
//                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
//                }
//            } else {
//                Notification.show("Invalid credentials", Notification.Type.ERROR_MESSAGE);
//            }
//        });
		content.setSizeUndefined();
		content.setMargin(true);
		panel.setContent(content);
		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
	}

	@Override
	@Autowired
	public void addListener(RegistrationView.ViewListener viewListener) {
		viewListener.setView(this);
		this.listeners.add(viewListener);
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
