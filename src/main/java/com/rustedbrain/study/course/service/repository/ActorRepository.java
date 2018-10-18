package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.cinema.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
}
