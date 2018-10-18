package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.cinema.CommentReputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentReputationRepository extends JpaRepository<CommentReputation, Long> {

	@Transactional
	@Modifying
	@Query("update CommentReputation cr set cr.plus = ?2 where cr.id = ?1")
	void setPlus(long id, boolean plus);
}
