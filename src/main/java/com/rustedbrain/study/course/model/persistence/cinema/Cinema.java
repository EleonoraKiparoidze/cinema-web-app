package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cinema")
public class Cinema extends DatabaseEntity {

	private static final long serialVersionUID = 5328622101331544154L;
	@Column(name = "name", length = 128, nullable = false)
	private String name;
	@OneToMany(mappedBy = "cinema")
	@Cascade({ org.hibernate.annotations.CascadeType.DELETE })
	private Set<FilmScreening> filmScreenings = new HashSet<>();
	@ManyToMany
	@JoinTable(name = "cinemaFeature", joinColumns = @JoinColumn(name = "cinemaId"), inverseJoinColumns = @JoinColumn(name = "featureId"))
	private Set<Feature> features = new HashSet<>();
	@OneToMany(mappedBy = "cinema")
	@Cascade({ org.hibernate.annotations.CascadeType.DELETE })
	private Set<CinemaHall> cinemaHalls = new HashSet<>();
	@ManyToOne
	private Manager manager;
	@ManyToOne
	private City city;
	@Column(name = "location", length = 512, nullable = false)
	private String location;

	public Cinema() {
	}

	public Cinema(String name, City city) {
		this.name = name;
		this.city = city;
	}

	public Cinema(City city, String name, String location, Manager manager) {
		this.city = city;
		this.name = name;
		this.location = location;
		this.manager = manager;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		if ( !super.equals(o) )
			return false;

		Cinema cinema = (Cinema) o;

		return name.equals(cinema.name) && city.equals(cinema.city);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + city.hashCode();
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<FilmScreening> getFilmScreenings() {
		return filmScreenings;
	}

	public void setFilmScreenings(Set<FilmScreening> filmScreenings) {
		this.filmScreenings = filmScreenings;
	}

	public Set<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}

	public Set<CinemaHall> getCinemaHalls() {
		return cinemaHalls;
	}

	public void setCinemaHalls(Set<CinemaHall> cinemaHalls) {
		this.cinemaHalls = cinemaHalls;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Cinema{" + "name='" + name + '\'' + ", city=" + city + '}';
	}
}
