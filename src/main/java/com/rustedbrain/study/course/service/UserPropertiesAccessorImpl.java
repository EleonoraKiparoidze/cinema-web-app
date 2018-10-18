package com.rustedbrain.study.course.service;

import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.service.repository.UserRepository;

import java.util.Date;
import java.util.List;

public class UserPropertiesAccessorImpl<T extends User> implements UserPropertiesAccessor<T> {

	private UserRepository<T> repository;

	UserPropertiesAccessorImpl(UserRepository<T> repository) {
		this.repository = repository;
	}

	@Override
	public void changeUserName(long userId, String name) {
		repository.changeUserName(userId, name);
	}

	@Override
	public void changeUserSurname(long userId, String surname) {
		repository.changeUserSurname(userId, surname);
	}

	@Override
	public void changeUserPassword(long userId, String password) {
		repository.changeUserPassword(userId, password);
	}

	@Override
	public void changeUserMail(long userId, String mail) {
		repository.changeUserMail(userId, mail);
	}

	@Override
	public void changeUserBirthday(long userId, Date birthday) {
		repository.changeUserBirthday(userId, birthday);
	}

	@Override
	public void changeUserCity(long userId, long cityId) {
		repository.changeUserCity(userId, cityId);
	}

	@Override
	public void changeUserLogin(long id, String login) {
		repository.changeUserLogin(id, login);
	}

	@Override
	public List<T> findAll() {
		return repository.findAll();
	}

	@Override
	public void changeUserBlockUntilDateAndDescription(long id, Date blockDate, String description) {
		repository.changeUserBlockDate(id, blockDate, description);
	}
}