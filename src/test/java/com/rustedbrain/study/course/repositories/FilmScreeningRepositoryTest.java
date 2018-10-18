package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.service.repository.FilmScreeningRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmScreeningRepositoryTest {

	@Autowired
	private FilmScreeningRepository repository;

	private FilmScreening filmScreening;

	@Before
	public void setUp() throws Exception {
		Date date = Date.from(Instant.now());

		filmScreening = new FilmScreening();
		filmScreening.setStartDate(java.sql.Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
		filmScreening.setEndDate(new java.sql.Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(14)));
		filmScreening.setRegistrationDate(date);
		filmScreening.setLastAccessDate(date);
	}

	@Test
	public void saveFeature() throws Exception {
		Assert.assertNotNull(repository.save(filmScreening));
	}

	@Test
	public void deleteFeature() throws Exception {
		repository.save(filmScreening);
		repository.delete(filmScreening);
		Assert.assertEquals(Optional.empty(), repository.findById(filmScreening.getId()));
	}

	@After
	public void tearDown() throws Exception {
		repository.deleteAllInBatch();
	}
}
