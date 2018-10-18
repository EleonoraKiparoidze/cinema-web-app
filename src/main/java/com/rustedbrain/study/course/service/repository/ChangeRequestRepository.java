package com.rustedbrain.study.course.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.rustedbrain.study.course.model.persistence.authorization.ChangeRequest;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, Long> {

	@Transactional
	@Modifying
	@Query("update ChangeRequest req set req.accepted = ?2 where req.id = ?1")
	void editAcceptance(long id, boolean accepted);

}
