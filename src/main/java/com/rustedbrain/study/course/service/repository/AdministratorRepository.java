package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.authorization.Administrator;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdministratorRepository extends UserRepository<Administrator> {

}
