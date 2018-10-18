package com.rustedbrain.study.course.service;

import java.util.List;
import java.util.Optional;

import com.rustedbrain.study.course.model.dto.AuthUser;
import com.rustedbrain.study.course.model.dto.UserInfo;
import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.authorization.Administrator;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.authorization.User;

public interface AuthorizationUserService {

	Optional<AuthUser> getIdentifiedAuthUser(String login, String password);

	Optional<AuthUser> getAuthUserById(long id);

	String rememberUser(String login, UserRole role);

	AuthUser getRememberedUser(String id);

	void removeRememberedUser(String id);

	boolean isValidCinemaManager(String userLogin, long cinemaId);

	Optional<Administrator> getAdministrator(String userLogin);

	UserPropertiesAccessor getUserPropertiesAccessor(UserRole role);

	User getUser(String login);

	Optional<UserInfo> getUserInfo(long userId);

	List<Manager> getManagers();
}
