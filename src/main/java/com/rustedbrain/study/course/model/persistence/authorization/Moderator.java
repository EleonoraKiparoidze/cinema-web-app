package com.rustedbrain.study.course.model.persistence.authorization;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "moderator")
public class Moderator extends User {

	private static final long serialVersionUID = 741920217200783406L;

	public Moderator() {
	}

	public Moderator(String login, String password, String mail) {
		super(login, password, mail);
	}
}
