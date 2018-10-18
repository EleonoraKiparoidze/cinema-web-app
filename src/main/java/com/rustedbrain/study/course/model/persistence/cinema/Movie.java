package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie extends DatabaseEntity {

	private static final long serialVersionUID = 9179493669036384025L;
	@Column(name = "localizedName", length = 128, nullable = false)
	private String localizedName;
	@Column(name = "originalName", length = 128, nullable = false)
	private String originalName;
	@Column(name = "country", length = 128)
	private String country;
	@Column(name = "producer", length = 128)
	private String producer;
	@Column(name = "releaseDate", nullable = false)
	private Date releaseDate;
	@ManyToMany
	@JoinTable(name = "movieGenre", joinColumns = @JoinColumn(name = "movieId"), inverseJoinColumns = @JoinColumn(name = "genreId"))
	private Set<Genre> genres = new HashSet<>();
	@Column(name = "description", length = 2048)
	private String description;
	@Column(name = "trailerURL", length = 256)
	private String trailerURL;
	@Column(name = "posterPath", length = 256)
	private String posterPath;
	@Column(name = "minAge")
	private int minAge;
	@Column(name = "timeMinutes")
	private int timeMinutes;
	@ManyToMany(fetch = FetchType.EAGER, cascade = javax.persistence.CascadeType.ALL)
	@JoinTable(name = "movieActor", joinColumns = @JoinColumn(name = "movieId"), inverseJoinColumns = @JoinColumn(name = "actorId"))
	private Set<Actor> actors = new HashSet<>();
	@OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<Comment> comments = new HashSet<>();

	public String getTrailerURL() {
		return trailerURL;
	}

	public void setTrailerURL(String trailerURL) {
		this.trailerURL = trailerURL;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getLocalizedName() {
		return localizedName;
	}

	public void setLocalizedName(String localizedName) {
		this.localizedName = localizedName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getTimeMinutes() {
		return timeMinutes;
	}

	public void setTimeMinutes(int timeMinutes) {
		this.timeMinutes = timeMinutes;
	}

	public Set<Actor> getActors() {
		return actors;
	}

	public void setActors(Set<Actor> actors) {
		this.actors = actors;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		if ( !super.equals(o) )
			return false;

		Movie movie = (Movie) o;

		return originalName.equals(movie.originalName) && releaseDate.equals(movie.releaseDate);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + originalName.hashCode();
		result = 31 * result + releaseDate.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Movie{" + "localizedName='" + localizedName + '\'' + ", originalName='" + originalName + '\''
				+ ", releaseDate=" + releaseDate + ", genres=" + genres + '}';
	}
}
