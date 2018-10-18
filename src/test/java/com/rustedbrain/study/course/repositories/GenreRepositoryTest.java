package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.Genre;
import com.rustedbrain.study.course.service.repository.GenreRepository;
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
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    private Genre genre;

    @Before
    public void setUp() throws Exception {
        genre = new Genre();
        genre.setId(1);
        genre.setLastAccessDate(new Date());
        genre.setName("Horror");
        genre.setRegistrationDate(new Date());
    }

    @Test
    public void saveGenre() throws Exception {
        Assert.assertNotNull(repository.save(genre));
    }

    @Test
    public void deleteGenre() throws Exception {
        repository.save(genre);
        repository.delete(genre);
        Assert.assertEquals(Optional.empty(), repository.findById(genre.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }
}
