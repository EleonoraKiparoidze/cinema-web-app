package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface FilmScreeningRepository extends JpaRepository<FilmScreening, Long> {

	@Query("select fs from FilmScreening fs where fs.cinema.id = ?1 and fs.endDate > ?2")
	List<FilmScreening> getFilmScreening(long cinemaId, Date currDate);

	@Transactional
	@Modifying
	@Query("update FilmScreening fs set fs.movie = ?2, fs.cinema = ?3, fs.startDate = ?4, fs.endDate = ?5 where fs.id = ?1")
	void editFilmScreening(long id, Movie movie, Cinema cinema, Date startDate, Date endDate);
}
