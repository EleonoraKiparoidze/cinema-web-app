package com.rustedbrain.study.course.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;

public interface CinemaHallRepository extends JpaRepository<CinemaHall, Long> {

	@Transactional
	@Modifying
	@Query("update CinemaHall cinemaHall set cinemaHall.name = ?2, cinemaHall.cinema = ?3 where cinemaHall.id = ?1")
	void editCinemaHall(long selectedCinemaHallId, String newCinemaHallName, Cinema newCinema);
}
