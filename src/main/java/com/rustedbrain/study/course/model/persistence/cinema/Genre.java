package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre extends DatabaseEntity {

	private static final long serialVersionUID = -3579725893493586217L;
	@Column(name = "name", length = 64, nullable = false)
	private String name;
	@ManyToMany(mappedBy = "genres")
	private Set<Movie> movies = new HashSet<>();

	public Genre(String name) {
		this.name = name;
	}

	public Genre() {
	}

	public Set<Movie> getMovies() {
		return movies;
	}

	public void setMovies(Set<Movie> movies) {
		this.movies = movies;
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

		Genre genre = (Genre) o;

		return name.equals(genre.name);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Genre{" + "name='" + name + '\'' + '}';
	}
}
