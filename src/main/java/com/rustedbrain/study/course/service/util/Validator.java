package com.rustedbrain.study.course.service.util;

import java.util.regex.Pattern;

public enum Validator {

	MAIL_VALIDATOR("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"),
	LOGIN_VALIDATOR("^[A-Za-z0-9-]+"); // TODO create pattern to validate users by login

	private Pattern pattern;

	Validator(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	public boolean isValid(final String hex) {
		return pattern.matcher(hex).matches();
	}
}
