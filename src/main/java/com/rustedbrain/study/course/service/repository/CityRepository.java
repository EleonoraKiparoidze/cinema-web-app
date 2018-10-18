package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CityRepository extends JpaRepository<City, Long> {

	@Query("select c from City c where c.name = ?1")
	City findByName(String name);

	@Transactional
	@Modifying
	@Query("update City city set city.name = ?2 where city.id = ?1")
	void renameCity(long id, String value);
}
