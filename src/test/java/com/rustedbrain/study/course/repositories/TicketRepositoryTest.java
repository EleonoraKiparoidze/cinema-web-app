package com.rustedbrain.study.course.repositories;

import com.rustedbrain.study.course.model.persistence.cinema.Ticket;
import com.rustedbrain.study.course.service.repository.TicketRepository;
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
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository repository;

    private Ticket ticket;

    @Before
    public void setUp() throws Exception {
        ticket = new Ticket();
        ticket.setSoldDate(new Date());
        ticket.setRegistrationDate(new Date());
    }

    @Test
    public void saveTicket() throws Exception {
        Assert.assertNotNull(repository.save(ticket));
    }

    @Test
    public void deleteTicket() throws Exception {
        repository.save(ticket);
        repository.delete(ticket);
        Assert.assertEquals(Optional.empty(), repository.findById(ticket.getId()));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllInBatch();
    }

}
