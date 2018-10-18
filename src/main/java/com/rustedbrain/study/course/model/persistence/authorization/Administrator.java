package com.rustedbrain.study.course.model.persistence.authorization;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "administrator")
public class Administrator extends User {

	private static final long serialVersionUID = 8148254296836712739L;

	public Administrator() {
	}

	public Administrator(String login, String password, String mail) {
		super(login, password, mail);
	}
}
