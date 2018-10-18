package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.Feature;
import com.rustedbrain.study.course.service.repository.FeatureRepository;
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
public class FeatureRepositoryTest {

    @Autowired
    private FeatureRepository repository;

    private Feature feature;

    @Before
    public void setUp() throws Exception {
        Date date = Date.from(Instant.now());

        feature = new Feature();
        feature.setName("Cinema Feature for test");
        feature.setRegistrationDate(date);
        feature.setLastAccessDate(date);
    }

    @Test
    public void saveFeature() throws Exception {
        Assert.assertNotNull(repository.save(feature));
    }

    @Test
    public void deleteFeature() throws Exception {
        repository.save(feature);
        repository.delete(feature);
        Assert.assertEquals(Optional.empty(), repository.findById(feature.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }
}
