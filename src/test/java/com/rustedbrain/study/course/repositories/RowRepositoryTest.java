package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.Row;
import com.rustedbrain.study.course.service.repository.RowRepository;
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
public class RowRepositoryTest {

    @Autowired
    private RowRepository repository;

    private Row row;

    @Before
    public void setUp() throws Exception {
        row = new Row();
        row.setRegistrationDate(new Date());
        row.setLastAccessDate(new Date());
        row.setNumber(13);
    }

    @Test
    public void saveRow() throws Exception {
        Assert.assertNotNull(repository.save(row));
    }

    @Test
    public void deleteRow() throws Exception {
        repository.save(row);
        repository.delete(row);
        Assert.assertEquals(Optional.empty(), repository.findById(row.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }
}
