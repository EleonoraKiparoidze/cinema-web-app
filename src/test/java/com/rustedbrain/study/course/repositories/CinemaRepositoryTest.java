package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.service.repository.CinemaRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CinemaRepositoryTest {

    @Autowired
    private CinemaRepository repository;

    private Cinema cinema;

    @Before
    public void setUp() throws Exception {
        cinema = new Cinema();
        cinema.setName("ТРЦ \"Hollywood\"");
        Date date = new Date();
        cinema.setRegistrationDate(date);
        cinema.setLastAccessDate(date);
    }

    @Test
    public void saveCinema() throws Exception {
        Assert.assertNotNull(repository.save(cinema));
    }

    @Test
    public void findCinemaByName() throws Exception {
        repository.save(cinema);
        Assert.assertEquals(cinema.getName(), repository.findByName(cinema.getName()).getName());
    }

    @Test
    public void deleteCinema() throws Exception {
        repository.save(cinema);
        repository.delete(cinema);
        Assert.assertEquals(Optional.empty(), repository.findById(cinema.getId()));
    }

    @Test
    public void updateCinema() throws Exception {
        repository.save(cinema);
        Cinema updateCinema = repository.getOne(cinema.getId());
        updateCinema.setName("New cinema name");
        repository.save(updateCinema);
        Assert.assertEquals("New cinema name", repository.getOne(cinema.getId()).getName());
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }
}
