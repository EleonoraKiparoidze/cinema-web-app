package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.service.repository.SeatRepository;
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
public class SeatRepositoryTest {


    @Autowired
    private SeatRepository repository;

    private Seat seat;

    @Before
    public void setUp() throws Exception {
        seat = new Seat();
        seat.setRegistrationDate(new Date());
        seat.setLastAccessDate(new Date());
        seat.setPrice(10);
        seat.setClientCount(10);
        seat.setNumber(13);
    }

    @Test
    public void saveSeat() throws Exception {
        Assert.assertNotNull(repository.save(seat));
    }

    @Test
    public void deleteSeat() throws Exception {
        repository.save(seat);
        repository.delete(seat);
        Assert.assertEquals(Optional.empty(), repository.findById(seat.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }
}
