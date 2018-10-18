package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.authorization.Paymaster;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PaymasterRepository extends UserRepository<Paymaster> {
}
