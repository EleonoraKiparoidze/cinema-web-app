package com.rustedbrain.study.course.view.authentication;

import com.rustedbrain.study.course.view.VaadinUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@SpringView(name = VaadinUI.LOGIN_VIEW)
public class LoginViewImpl extends VerticalLayout implements LoginView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1627951144681518871L;
	private List<LoginViewListener> loginViewListeners = new ArrayList<>();

	public LoginViewImpl() {
		Panel loginPanel = getLoginPanel();
		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}

	private Panel getLoginPanel() {
		Panel panel = new Panel("Login");
		panel.setSizeUndefined();

		FormLayout content = new FormLayout();
		TextField loginTextField = new TextField("Login");
		content.addComponent(loginTextField);
		TextField passwordTextField = new PasswordField("Password");
		content.addComponent(passwordTextField);
		CheckBox checkBoxRemember = new CheckBox("Remember me");
		content.addComponent(checkBoxRemember);

		content.addComponent(new Button("Login",
				(Button.ClickListener) event -> loginViewListeners
						.forEach(loginViewListener -> loginViewListener.loginButtonClicked(loginTextField.getValue(),
								passwordTextField.getValue(), checkBoxRemember.getValue()))));
		content.setSizeUndefined();
		content.setMargin(true);
		panel.setContent(content);
		return panel;
	}

	@Override
	@Autowired
	public void addListener(LoginViewListener listener) {
		listener.setLoginView(this);
		loginViewListeners.add(listener);
	}

	@Override
	public void showInvalidCredentialsNotification() {
		Notification.show("Invalid authorization data specified. Please, check your login and password.",
				Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void showError(String message) {
		Notification.show(message, Notification.Type.ERROR_MESSAGE);
	}
}