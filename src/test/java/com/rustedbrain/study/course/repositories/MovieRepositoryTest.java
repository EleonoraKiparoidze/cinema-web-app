package com.rustedbrain.study.course.repositories;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rustedbrain.study.course.model.persistence.cinema.Actor;
import com.rustedbrain.study.course.model.persistence.cinema.Genre;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.repository.MovieRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieRepositoryTest {

	@Autowired
	private MovieRepository repository;

	private Movie movie;

	@Before
	public void setUp() throws Exception {
		movie = new Movie();
		movie.setLocalizedName("Великий Гэтсби");
		movie.setOriginalName("The Great Gatsby");
		movie.setMinAge(13);
		movie.setRegistrationDate(new Date());
	}

	@Test
	public void saveMovie() throws Exception {
		Assert.assertNotNull(repository.save(movie));
	}

	@Test
	public void deleteMovie() throws Exception {
		repository.save(movie);
		repository.delete(movie);
		Assert.assertEquals(Optional.empty(), repository.findById(movie.getId()));
	}

	@Test
	public void findMovieByOriginalName() throws Exception {
		repository.save(movie);
		Assert.assertNotNull(repository.findByOriginalName("The Great Gatsby"));
	}

	@Test
	public void cascadePersistMovieGenres() throws Exception {
		Date date = Date.from(Instant.now());

		Genre genre1 = new Genre();
		genre1.setName("adventure");
		genre1.setRegistrationDate(date);
		genre1.setLastAccessDate(date);

		Genre genre2 = new Genre();
		genre2.setName("comedy");
		genre2.setRegistrationDate(date);
		genre2.setLastAccessDate(date);

		Genre genre3 = new Genre();
		genre3.setName("drama");
		genre3.setRegistrationDate(date);
		genre3.setLastAccessDate(date);

		Genre genre4 = new Genre();
		genre4.setName("horror");
		genre4.setRegistrationDate(date);
		genre4.setLastAccessDate(date);

		movie.setGenres(new HashSet<>(Arrays.asList(genre1, genre2, genre3, genre4)));

		repository.save(movie);

		Assert.assertFalse(repository.getOne(movie.getId()).getGenres().isEmpty());
	}

	@Test
	public void cascadePersistMovieActors() throws Exception {
		Date date = Date.from(Instant.now());

		Actor actor1 = new Actor();
		actor1.setName("Orlando");
		actor1.setSurname("Bloom");
		actor1.setRegistrationDate(date);
		actor1.setLastAccessDate(date);

		Actor actor2 = new Actor();
		actor2.setName("John");
		actor2.setSurname("Depp");
		actor2.setRegistrationDate(date);
		actor2.setLastAccessDate(date);

		movie.setActors(new HashSet<>(Arrays.asList(actor1, actor2)));

		repository.save(movie);

		Assert.assertFalse(repository.getOne(movie.getId()).getActors().isEmpty());
	}

	@Test
	public void findMovieByLocalizedName() throws Exception {
		repository.save(movie);
		Assert.assertNotNull(repository.findByLocalizedName("Великий Гэтсби"));
	}

	@After
	public void tearDown() throws Exception {
		repository.deleteAllInBatch();
	}
}