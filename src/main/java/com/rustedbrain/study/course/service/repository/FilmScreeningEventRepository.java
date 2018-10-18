package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface FilmScreeningEventRepository extends JpaRepository<FilmScreeningEvent, Long> {

	@Query("select fs from FilmScreeningEvent fs where fs.filmScreening.cinema.id = ?1 and fs.date = ?2")
	List<FilmScreeningEvent> getFilmScreeningEvents(long cinemaId, Date currDate);

}
