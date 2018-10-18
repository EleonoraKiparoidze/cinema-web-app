package com.rustedbrain.study.course.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rustedbrain.study.course.model.persistence.cinema.Row;

public interface RowRepository extends JpaRepository<Row, Long> {

}
