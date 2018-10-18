package com.rustedbrain.study.course.service;

import com.rustedbrain.study.course.model.persistence.authorization.User;

import java.util.Date;
import java.util.List;

public interface UserPropertiesAccessor<T extends User> {

	void changeUserName(long userId, String name);

	void changeUserSurname(long userId, String surname);

	void changeUserPassword(long userId, String password);

	void changeUserMail(long userId, String mail);

	void changeUserBirthday(long userId, Date birthday);

	void changeUserCity(long userId, long city);

	void changeUserLogin(long id, String login);

	List<?> findAll();

	void changeUserBlockUntilDateAndDescription(long id, Date blockDate, String description);
}
