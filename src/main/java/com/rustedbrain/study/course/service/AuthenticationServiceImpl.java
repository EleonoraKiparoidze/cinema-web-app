package com.rustedbrain.study.course.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rustedbrain.study.course.model.dto.AuthUser;
import com.rustedbrain.study.course.model.dto.UserInfo;
import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.authorization.ChangeRequest;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.service.repository.ChangeRequestRepository;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final String SESSION_USERNAME = "username";
	private static final String SESSION_USER_ROLE = "user_role";
	private static final String COOKIE_NAME = "remember-me";

	private AuthorizationUserService authorizationUserService;
	private ChangeRequestRepository changeRequestRepository;

	@Autowired
	public void setChangeRequestRepository(ChangeRequestRepository changeRequestRepository) {
		this.changeRequestRepository = changeRequestRepository;
	}

	private static Optional<Cookie> getRememberMeCookie() {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		return Arrays.stream(cookies).filter(c -> c.getName().equals(COOKIE_NAME)).findFirst();
	}

	public boolean isCinemaManagementAvailable(long cinemaId) {
		switch (getUserRole()) {
		case ADMINISTRATOR:
			return true;
		case MEMBER:
			return false;
		case MANAGER:
			return authorizationUserService.isValidCinemaManager(getUserLogin(), cinemaId);
		case PAYMASTER:
			return false;
		case MODERATOR:
			return false;
		case NOT_AUTHORIZED:
			return false;
		default:
			throw new IllegalStateException("Something is going wrong. Role behaviour not implemented yet.");
		}
	}

	public UserRole getUserRole() {
		if ( isAuthenticated() ) {
			return UserRole.valueOf(VaadinSession.getCurrent().getAttribute(SESSION_USER_ROLE).toString());
		} else {
			return UserRole.NOT_AUTHORIZED;
		}
	}

	public String getUserLogin() {
		return VaadinSession.getCurrent().getAttribute(SESSION_USERNAME).toString();
	}

	@Override
	public User getAuthenticUser() {
		if ( !isAuthenticated() ) {
			throw new IllegalStateException("Cannot retrieve not authenticated user.");
		}
		Optional<User> optionalUser = Optional.ofNullable(authorizationUserService.getUser(getUserLogin()));
		if ( !optionalUser.isPresent() ) {
			throw new IllegalStateException("User with specified login/mail not found.");
		} else {
			return optionalUser.get();
		}
	}

	@Override
	public List<User> getUsersByRoles(List<UserRole> roles) {
		return roles.stream().flatMap(
				role -> ((List<User>) authorizationUserService.getUserPropertiesAccessor(role).findAll()).stream())
				.collect(Collectors.toList());
	}

	@Override
	public void changeUserName(long id, String name, UserRole authorizedUserRole) {
		switch (authorizedUserRole) {
		case ADMINISTRATOR: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserName(id, name);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		default: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				String userRole = optionalAuthUser.get().getUserRole().name().toLowerCase();
				String tableName = Character.toUpperCase(userRole.charAt(0)) + userRole.substring(1);
				ChangeRequest changeRequest = new ChangeRequest(tableName, "name", id, name);
				changeRequestRepository.save(changeRequest);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		}
	}

	@Override
	public void changeUserLogin(long id, String login, UserRole role) {
		switch (role) {
		case ADMINISTRATOR: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserLogin(id, login);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		default: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				String userRole = optionalAuthUser.get().getUserRole().name().toLowerCase();
				String tableName = Character.toUpperCase(userRole.charAt(0)) + userRole.substring(1);
				ChangeRequest changeRequest = new ChangeRequest(tableName, "login", id, login);
				changeRequestRepository.save(changeRequest);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		}
	}

	@Override
	public void changeUserMail(long id, String mail, UserRole role) {
		switch (role) {
		case ADMINISTRATOR: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserMail(id, mail);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		default: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				String userRole = optionalAuthUser.get().getUserRole().name().toLowerCase();
				String tableName = Character.toUpperCase(userRole.charAt(0)) + userRole.substring(1);
				ChangeRequest changeRequest = new ChangeRequest(tableName, "mail", id, mail);
				changeRequestRepository.save(changeRequest);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		}
	}

	@Override
	public void changeUserSurname(long id, String surname, UserRole role) {
		switch (role) {
		case ADMINISTRATOR: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserSurname(id, surname);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		default: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				String userRole = optionalAuthUser.get().getUserRole().name().toLowerCase();
				String tableName = Character.toUpperCase(userRole.charAt(0)) + userRole.substring(1);
				ChangeRequest changeRequest = new ChangeRequest(tableName, "surname", id, surname);
				changeRequestRepository.save(changeRequest);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		}
	}

	@Override
	public void changeUserCity(long id, long cityId, UserRole role) {
		switch (role) {
		case ADMINISTRATOR: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserCity(id, cityId);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		default: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				String userRole = optionalAuthUser.get().getUserRole().name().toLowerCase();
				String tableName = Character.toUpperCase(userRole.charAt(0)) + userRole.substring(1);
				ChangeRequest changeRequest = new ChangeRequest(tableName, "city", id, String.valueOf(cityId));
				changeRequestRepository.save(changeRequest);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		}
	}

	@Override
	public void changeUserBirthday(long id, LocalDate birthday, UserRole role) {
		switch (role) {
		case ADMINISTRATOR: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserBirthday(id, Date.from(birthday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		default: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				String userRole = optionalAuthUser.get().getUserRole().name().toLowerCase();
				String tableName = Character.toUpperCase(userRole.charAt(0)) + userRole.substring(1);
				String birthdayFormat = birthday.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH));
				ChangeRequest changeRequest = new ChangeRequest(tableName, "birthday", id, birthdayFormat);
				changeRequestRepository.save(changeRequest);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		}
	}

	@Override
	public void changeUserBlockUntilDate(long id, LocalDateTime blockedUntilDate, String blockDescription,
			UserRole userRole) {
		switch (userRole) {
		case ADMINISTRATOR:
		case MODERATOR: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserBlockUntilDateAndDescription(id,
								Date.from(blockedUntilDate.atZone(ZoneId.systemDefault()).toInstant()),
								blockDescription);
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		default: {
			throw new IllegalArgumentException("Only administrator and moderator can block another users.");
		}
		}
	}

	@Override
	public void unblockUser(long id, UserRole userRole) {
		switch (userRole) {
		case ADMINISTRATOR:
		case MODERATOR: {
			Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(id);
			if ( optionalAuthUser.isPresent() ) {
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserBlockUntilDateAndDescription(id, Date.from(Instant.now()), "");
			} else {
				throw new IllegalArgumentException("User with specified id not found.");
			}
		}
			break;
		default: {
			throw new IllegalArgumentException("Only administrator and moderator can unblock another users.");
		}
		}
	}

	@Override
	public Optional<AuthUser> getAuthUserById(long id) {
		return authorizationUserService.getAuthUserById(id);
	}

	@Override
	public Optional<UserInfo> getUserInfo(long userId) {
		return authorizationUserService.getUserInfo(userId);
	}

	@Override
	public boolean isAuthenticated() {
		return (VaadinSession.getCurrent().getAttribute(SESSION_USERNAME) != null
				&& VaadinSession.getCurrent().getAttribute(SESSION_USER_ROLE) != null) || loginRememberedUser();
	}

	public void logOut() {
		Optional<Cookie> cookie = getRememberMeCookie();
		if ( cookie.isPresent() ) {
			String id = cookie.get().getValue();
			authorizationUserService.removeRememberedUser(id);
			deleteRememberMeCookie();
		}

		VaadinSession.getCurrent().close();
		Page.getCurrent().setLocation("");
	}

	private boolean loginRememberedUser() {
		Optional<Cookie> rememberMeCookie = getRememberMeCookie();

		if ( rememberMeCookie.isPresent() ) {
			String id = rememberMeCookie.get().getValue();
			AuthUser authUser = authorizationUserService.getRememberedUser(id);

			if ( authUser != null ) {
				VaadinSession.getCurrent().setAttribute(SESSION_USERNAME, authUser.getLogin());
				VaadinSession.getCurrent().setAttribute(SESSION_USER_ROLE, authUser.getUserRole().name());
				return true;
			}
		}
		return false;
	}

	private void rememberUser(String username, UserRole role) {
		String id = authorizationUserService.rememberUser(username, role);

		Cookie cookie = new Cookie(COOKIE_NAME, id);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 30); // valid for 30 days
		VaadinService.getCurrentResponse().addCookie(cookie);
	}

	private void deleteRememberMeCookie() {
		Cookie cookie = new Cookie(COOKIE_NAME, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		VaadinService.getCurrentResponse().addCookie(cookie);
	}

	@Override
	public boolean login(String loginOrMail, String password, boolean rememberMe) {
		Optional<AuthUser> optionalUser = authorizationUserService.getIdentifiedAuthUser(loginOrMail, password);
		if ( optionalUser.isPresent() ) {
			AuthUser user = optionalUser.get();
			VaadinSession.getCurrent().setAttribute(SESSION_USERNAME, user.getLogin());
			VaadinSession.getCurrent().setAttribute(SESSION_USER_ROLE, user.getUserRole().name());
			if ( rememberMe ) {
				rememberUser(user.getLogin(), user.getUserRole());
			}
			return true;
		}
		return false;
	}

	@Autowired
	public void setAuthorizationUserService(AuthorizationUserService authorizationUserService) {
		this.authorizationUserService = authorizationUserService;
	}

	@Override
	public List<Manager> getManagers() {
		return authorizationUserService.getManagers();
	}

	@Override
	public List<ChangeRequest> getChangeRequests() {
		return changeRequestRepository.findAll();
	}

	@Override
	public void acceptChangeRequest(long userId, String fieldName, String value, long changeRequestId) {
		Optional<AuthUser> optionalAuthUser = authorizationUserService.getAuthUserById(userId);
		if ( optionalAuthUser.isPresent() ) {
			switch (fieldName) {
			case "surname":
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserSurname(userId, value);
				break;
			case "name":
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserName(userId, value);
				break;
			case "login":
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserLogin(userId, value);
				break;
			case "mail":
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserMail(userId, value);
				break;
			case "birthday": {
				authorizationUserService
						.getUserPropertiesAccessor(
								optionalAuthUser.get().getUserRole())
						.changeUserBirthday(userId,
								Date.from(LocalDate
										.parse(value, DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH))
										.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			}
				break;
			case "city":
				authorizationUserService.getUserPropertiesAccessor(optionalAuthUser.get().getUserRole())
						.changeUserCity(userId, Long.valueOf(value));
				break;
			}

			Optional<ChangeRequest> optionChangeRequest = changeRequestRepository.findById(changeRequestId);
			if ( optionChangeRequest.isPresent() ) {
				changeRequestRepository.editAcceptance(changeRequestId, true);
			}
		}

	}

	@Override
	public void declineChangeRequest(long userId, String fieldName, String value, long changeRequestId) {
		Optional<ChangeRequest> optionChangeRequest = changeRequestRepository.findById(changeRequestId);
		if ( optionChangeRequest.isPresent() ) {
			changeRequestRepository.deleteById(changeRequestId);
		}

	}
}
