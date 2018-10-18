package com.rustedbrain.study.course.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.rustedbrain.study.course.model.dto.AuthUser;
import com.rustedbrain.study.course.model.dto.UserInfo;
import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.authorization.ChangeRequest;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.authorization.User;

public interface AuthenticationService {

	boolean isCinemaManagementAvailable(long cinemaId);

	boolean isAuthenticated();

	void logOut();

	boolean login(String username, String password, boolean rememberMe);

	UserRole getUserRole();

	String getUserLogin();

	User getAuthenticUser();

	List<User> getUsersByRoles(List<UserRole> roles);

	void changeUserName(long id, String name, UserRole role);

	void changeUserLogin(long id, String login, UserRole role);

	void changeUserMail(long id, String mail, UserRole role);

	void changeUserSurname(long id, String surname, UserRole userRole);

	void changeUserCity(long id, long cityId, UserRole userRole);

	void changeUserBirthday(long id, LocalDate birthday, UserRole role);

	void changeUserBlockUntilDate(long id, LocalDateTime blockedUntilDate, String blockDescription, UserRole userRole);

	void unblockUser(long id, UserRole userRole);

	Optional<AuthUser> getAuthUserById(long id);

	Optional<UserInfo> getUserInfo(long userId);

	List<Manager> getManagers();

	List<ChangeRequest> getChangeRequests();

	void acceptChangeRequest(long userId, String fieldName, String value, long changeRequestId);

	void declineChangeRequest(long userId, String fieldName, String value, long changeRequestId);
}
