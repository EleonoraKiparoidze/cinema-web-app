package com.rustedbrain.study.course.service.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.rustedbrain.study.course.model.persistence.cinema.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	@Query("select m from Movie m where m.localizedName = ?1")
	Movie findByLocalizedName(String localizedName);

	@Query("select m from Movie m where m.originalName = ?1")
	Movie findByOriginalName(String originalName);

	@Transactional
	@Modifying
	@Query("update Movie movie set movie.originalName = ?2, movie.localizedName = ?3, movie.country = ?4, movie.releaseDate = ?5 where movie.id = ?1")
	void editMovie(long id, String newOriginalMovieName, String newLocalizeMovieName, String newCountry,
			Date releaseDate);
}
