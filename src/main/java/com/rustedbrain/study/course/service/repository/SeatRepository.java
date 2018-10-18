package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
