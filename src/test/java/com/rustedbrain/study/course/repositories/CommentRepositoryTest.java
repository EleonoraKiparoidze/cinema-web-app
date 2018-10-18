package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.authorization.Member;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Comment;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.repository.CityRepository;
import com.rustedbrain.study.course.service.repository.CommentRepository;
import com.rustedbrain.study.course.service.repository.MemberRepository;
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
import java.util.GregorianCalendar;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Comment comment;

    @Before
    public void setUp() throws Exception {
        Date date = Date.from(Instant.now());

        Movie movie = new Movie();
        movie.setLocalizedName("Великий Гэтсби");
        movie.setOriginalName("The Great Gatsby");
        movie.setMinAge(13);
        movie.setRegistrationDate(new Date());

        City city = new City("Chernigov");
        city.setLastAccessDate(date);
        city.setRegistrationDate(date);

        Member member = new Member();
        member.setEmail("alexeymuhinout@gmail.com");
        member.setSurname("Muhin");
        member.setName("Alexey");
        member.setBirthday(new GregorianCalendar(1996, 5, 29).getTime());
        member.setCity(city);
        member.setLogin("Bloodar");
        member.setPassword("Dif1(ulT0BRuT");
        member.setRegistrationDate(date);
        member.setLastAccessDate(date);

        comment = new Comment();
        comment.setUser(member);
        comment.setMessage("Hello all!!!");
        comment.setRegistrationDate(date);
        comment.setLastAccessDate(date);
        comment.setMovie(movie);

        repository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        cityRepository.deleteAllInBatch();
    }

    @Test
    public void saveComment() throws Exception {
        Assert.assertNotNull(repository.save(comment));
    }

    @Test
    public void deleteComment() throws Exception {
        repository.save(comment);
        repository.delete(comment);
        Assert.assertEquals(Optional.empty(), repository.findById(comment.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        cityRepository.deleteAllInBatch();
    }
}
