package com.rustedbrain.study.course.model.dto;

public class AuthUser {

	private String name;
	private UserRole userRole;

	public AuthUser(String name, UserRole userRole) {
		this.name = name;
		this.userRole = userRole;
	}

	public String getLogin() {
		return name;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	@Override
	public String toString() {
		return "RememberedUser{" + "name='" + name + '\'' + ", userRole=" + userRole + '}';
	}

}
