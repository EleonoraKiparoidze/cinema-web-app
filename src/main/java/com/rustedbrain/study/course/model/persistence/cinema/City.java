package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "city")
public class City extends DatabaseEntity implements Comparable<City> {

	private static final long serialVersionUID = -732856358445864096L;
	@Column(name = "name", length = 64, nullable = false, unique = true)
	private String name;
	@OneToMany(mappedBy = "city")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<Cinema> cinemas;
	@OneToMany(mappedBy = "city")
	private Set<User> users;

	public City() {
	}

	public City(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Cinema> getCinemas() {
		return cinemas;
	}

	public void setCinemas(Set<Cinema> cinemas) {
		this.cinemas = cinemas;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;

		City city = (City) o;

		return name.equals(city.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "City{" + "name='" + name + '\'' + '}';
	}

	@Override
	public int compareTo(City city) {
		return this.name.compareTo(city.name);
	}
}
