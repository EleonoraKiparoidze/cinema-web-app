package com.rustedbrain.study.course.model.persistence.authorization;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.Ticket;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "paymaster")
public class Paymaster extends User {

	private static final long serialVersionUID = -6870052176268087499L;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	private Cinema cinema;
	@OneToMany(mappedBy = "paymaster")
	private Set<Ticket> tickets;

	public Paymaster() {
	}

	public Paymaster(String login, String password, String mail) {
		super(login, password, mail);
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}
}
