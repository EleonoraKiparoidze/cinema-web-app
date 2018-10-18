package com.rustedbrain.study.course.presenter.cinema;

import com.rustedbrain.study.course.model.dto.UserInfo;
import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.ProfileInfoView;
import com.rustedbrain.study.course.view.util.PageNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

@UIScope
@SpringComponent
public class ProfileInfoViewPresenter implements ProfileInfoView.Listener, Serializable {

	private static final long serialVersionUID = -5683347947742290547L;
	private ProfileInfoView view;
	private final AuthenticationService authenticationService;
	private final CinemaService cinemaService;
	private UserInfo userInfo;

	@Autowired
	public ProfileInfoViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
		this.authenticationService = authenticationService;
	}

	@Override
	public void setView(ProfileInfoView view) {
		this.view = view;
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		Optional<String> optionalId = Optional.ofNullable(event.getParameters());
		if ( optionalId.isPresent() ) {
			Optional<UserInfo> optionalUserInfo = authenticationService.getUserInfo(Long.parseLong(optionalId.get()));
			if ( optionalUserInfo.isPresent() ) {
				this.userInfo = optionalUserInfo.get();
				if ( authenticationService.isAuthenticated() ) {
					this.view.showUserInfo(userInfo,
							(UserRole.MODERATOR.equals(authenticationService.getUserRole())
									|| UserRole.ADMINISTRATOR.equals(authenticationService.getUserRole())),
							cinemaService.getCities());
				} else {
					this.view.showNotAuthError();
				}
			} else {
				this.view.showError("User with specified id not exist.");
			}
		} else {
			this.view.showError("User id not presented.");
		}
	}

	@Override
	public void buttonAuthorizeClicked() {
		new PageNavigator().navigateToLoginView();
	}

	@Override
	public void buttonRegisterClicked() {
		new PageNavigator().navigateToRegistrationView();
	}

	@Override
	public void buttonChangeLoginClicked(long id, String login) {
		authenticationService.changeUserLogin(id, login, authenticationService.getUserRole());
	}

	@Override
	public void buttonChangeMailClicked(long id, String mail) {
		authenticationService.changeUserMail(id, mail, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonChangeSurnameClicked(long id, String surname) {
		authenticationService.changeUserSurname(id, surname, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonChangeCityClicked(long id, long cityId) {
		authenticationService.changeUserCity(id, cityId, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonChangeBirthdayClicked(long id, LocalDate birthday) {
		authenticationService.changeUserBirthday(id, birthday, authenticationService.getUserRole());
		view.reload();
	}

	@Override
	public void buttonChangeNameClicked(long id, String name) {
		authenticationService.changeUserName(id, name, authenticationService.getUserRole());
		view.reload();
	}

}
