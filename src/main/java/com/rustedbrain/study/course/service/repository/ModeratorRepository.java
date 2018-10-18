package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.authorization.Moderator;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ModeratorRepository extends UserRepository<Moderator> {
}
