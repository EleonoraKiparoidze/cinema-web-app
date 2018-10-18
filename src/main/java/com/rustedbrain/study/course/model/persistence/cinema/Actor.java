package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actor")
public class Actor extends DatabaseEntity {

	private static final long serialVersionUID = -5602366074810575732L;
	@Column(name = "name", length = 64)
	private String name;
	@Column(name = "surname", length = 64)
	private String surname;
	@ManyToMany(mappedBy = "actors", cascade = { CascadeType.ALL })
	private Set<Movie> movies = new HashSet<>();

	public Actor(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public Actor() {
	}

	public Set<Movie> getMovies() {
		return movies;
	}

	public void setMovies(Set<Movie> movies) {
		this.movies = movies;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		if ( !super.equals(o) )
			return false;

		Actor actor = (Actor) o;

		return name.equals(actor.name) && surname.equals(actor.surname);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + surname.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Actor{" + "name='" + name + '\'' + ", surname='" + surname + '\'' + '}';
	}
}
