package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "feature")
public class Feature extends DatabaseEntity {

	private static final long serialVersionUID = -913536847436205300L;
	@Column(name = "name", length = 32, nullable = false)
	private String name;
	@Column(name = "featureDescription", length = 256)
	private String featureDescription;
	@ManyToMany(mappedBy = "features")
	private Set<Cinema> cinemas;

	public Feature(String name) {
		this.name = name;
	}

	public Feature() {
	}

	public Feature(String name, String featureDescription) {
		this(name);
		this.featureDescription = featureDescription;
	}

	public Set<Cinema> getCinemas() {
		return cinemas;
	}

	public void setCinemas(Set<Cinema> cinemas) {
		this.cinemas = cinemas;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFeatureDescription() {
		return featureDescription;
	}

	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		if ( !super.equals(o) )
			return false;

		Feature that = (Feature) o;

		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Feature{" + "name='" + name + '\'' + ", featureDescription='" + featureDescription + '\'' + ", cinemas="
				+ cinemas + "} " + super.toString();
	}
}
