package com.rustedbrain.study.course.view.authentication;

import com.vaadin.navigator.View;

public interface LoginView extends View {

	void addListener(LoginViewListener listener);

	void showInvalidCredentialsNotification();

	void showError(String message);

	interface LoginViewListener {

		void loginButtonClicked(String login, String password, boolean rememberMe);

		void setLoginView(LoginView loginView);
	}
}
