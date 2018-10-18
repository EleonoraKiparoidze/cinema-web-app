package com.rustedbrain.study.course.model.dto;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.rustedbrain.study.course.model.persistence.authorization.User;

public class UserInfo {

	private final boolean banned;
	private final User user;
	private final String city;
	private final String login;
	private final String name;
	private final String surname;
	private final String birthday;
	private final String registrationDate;
	private final String lastAccessDate;
	private final long id;
	private UserRole role;

	public UserInfo(User user, UserRole role) {
		this.user = user;
		this.id = user.getId();
		this.login = user.getLogin();
		this.name = user.getName();
		this.surname = user.getSurname();
		this.birthday = user.getBirthday() != null
				? user.getBirthday().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE)
				: "N/A";
		this.registrationDate = user.getRegistrationDate() != null ? user.getRegistrationDate().toInstant()
				.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE) : "N/A";
		this.lastAccessDate = user.getLastAccessDate() != null ? user.getLastAccessDate().toInstant()
				.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE) : "N/A";
		this.city = user.getCity() != null ? user.getCity().getName() : "N/A";
		this.banned = user.getBlockPeriod() != null ? user.getBlockPeriod().after(new Date()) : false;
		this.role = role;
	}

	public User getUser() {
		return this.user;
	}

	public boolean isBanned() {
		return banned;
	}

	public String getCity() {
		return city;
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public String getLastAccessDate() {
		return lastAccessDate;
	}

	public long getId() {
		return id;
	}

	public UserRole getRole() {
		return role;
	}
}
