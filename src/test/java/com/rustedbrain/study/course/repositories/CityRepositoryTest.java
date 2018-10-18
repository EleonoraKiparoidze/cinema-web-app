package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.service.repository.CityRepository;
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
public class CityRepositoryTest {

    @Autowired
    private CityRepository repository;

    private City city;

    @Before
    public void setUp() throws Exception {
        city = new City();
        city.setName("Чернигов");
        city.setRegistrationDate(new Date());
    }

    @Test
    public void saveCity() throws Exception {
        Assert.assertNotNull(repository.save(city));
    }

    @Test
    public void deleteCity() throws Exception {
        repository.save(city);
        repository.delete(city);
        Assert.assertEquals(Optional.empty(), repository.findById(city.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }
}