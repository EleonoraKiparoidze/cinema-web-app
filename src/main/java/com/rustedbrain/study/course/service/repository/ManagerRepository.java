package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ManagerRepository extends UserRepository<Manager> {

	@Query("select count(m) from Manager m left join m.cinemas c where c.id = ?2 and m.login = ?1")
	boolean isCinemaManager(String login, long cinemaId);
}
