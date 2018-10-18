package com.rustedbrain.study.course.presenter.authentication;

import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.authentication.LoginView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.AccessControlException;
import java.util.logging.Logger;

@UIScope
@SpringComponent
public class LoginPresenter implements LoginView.LoginViewListener {

	private static final Logger logger = Logger.getLogger(LoginPresenter.class.getName());
	private final AuthenticationService authenticationService;
	private LoginView loginView;

	@Autowired
	public LoginPresenter(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public void loginButtonClicked(String login, String password, boolean rememberMe) {
		try {
			if (authenticationService.login(login, password, rememberMe)) {
				logger.info("User with login \"" + login + "\" successfully logged in. Navigating to main view.");
				new PageNavigator().navigateToMainView();
			} else {
				logger.info("User with login \"" + login + "\" and password \"" + password
						+ "\" not exist. Can not login.");
				loginView.showInvalidCredentialsNotification();
			}
		} catch (AccessControlException ex) {
			loginView.showError(ex.getMessage());
		}
	}

	@Override
	public void setLoginView(LoginView loginView) {
		this.loginView = loginView;
	}
}
