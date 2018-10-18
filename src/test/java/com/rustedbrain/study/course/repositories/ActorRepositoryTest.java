package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.Actor;
import com.rustedbrain.study.course.service.repository.ActorRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActorRepositoryTest {

    @Autowired
    private ActorRepository repository;

    private Actor actor;

    @Before
    public void setUp() throws Exception {
        Date date = Date.from(Instant.now());

        actor = new Actor();
        actor.setName("Orlando");
        actor.setSurname("Bloom");
        actor.setRegistrationDate(date);
        actor.setLastAccessDate(date);
    }

    @Test
    public void saveActor() throws Exception {
        Assert.assertNotNull(repository.save(actor));
    }

    @Test
    public void deleteActor() throws Exception {
        repository.save(actor);
        repository.delete(actor);
        Assert.assertEquals(Optional.empty(), repository.findById(actor.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }
}
