package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.cinema.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Transactional
	@Modifying
	@Query("update Comment comment set comment.message = ?2 where comment.id = ?1")
	void editComment(long commentId, String newValue);
}
