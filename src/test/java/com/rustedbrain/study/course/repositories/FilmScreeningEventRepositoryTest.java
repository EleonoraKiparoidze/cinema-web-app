package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.service.repository.FilmScreeningEventRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmScreeningEventRepositoryTest {


    @Autowired
    private FilmScreeningEventRepository repository;

    private FilmScreeningEvent filmScreeningEvent;

    @Before
    public void setUp() throws Exception {
        filmScreeningEvent = new FilmScreeningEvent();
        filmScreeningEvent.setRegistrationDate(new Date());
        filmScreeningEvent.setTime(new Time(21));
        filmScreeningEvent.setLastAccessDate(new Date());
    }

    @Test
    public void saveFilmScreeningEvent() throws Exception {
        Assert.assertNotNull(repository.save(filmScreeningEvent));
    }

    @Test
    public void deleteFilmScreeningEvent() throws Exception {
        repository.save(filmScreeningEvent);
        repository.delete(filmScreeningEvent);
        Assert.assertEquals(Optional.empty(), repository.findById(filmScreeningEvent.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }
}
