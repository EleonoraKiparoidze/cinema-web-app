package com.rustedbrain.study.course.repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rustedbrain.study.course.model.persistence.authorization.Member;
import com.rustedbrain.study.course.service.repository.MemberRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

	@Autowired
	private MemberRepository repository;

	private Member member;

	@Before
	public void setUp() throws Exception {
		member = new Member();
		member.setRegistrationDate(Date.valueOf(LocalDate.now()));
		member.setLastAccessDate(Date.valueOf(LocalDate.now()));
		member.setBirthday(Date.valueOf(LocalDate.of(1995, 10, 12)));
		member.setEmail("member@gmail.com");
		member.setName("Member");
		member.setSurname("MemberSurname");
		member.setLogin("member123");
		member.setPassword("123321");
	}

	@Test
	public void saveMember() throws Exception {
		Assert.assertNotNull(repository.save(member));
	}

	@Test
	public void deleteMember() throws Exception {
		repository.save(member);
		repository.delete(member);
		Assert.assertEquals(Optional.empty(), repository.findById(member.getId()));
	}

	@After
	public void tearDown() throws Exception {
		repository.deleteAllInBatch();
	}

}
