package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import com.rustedbrain.study.course.model.persistence.authorization.Member;
import com.rustedbrain.study.course.model.persistence.authorization.Paymaster;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ticket")
public class Ticket extends DatabaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2466332570578846063L;
	@ManyToOne(optional = false)
	private FilmScreeningEvent event;
	@Column(name = "soldDate")
	private Date soldDate;
	@Column(name = "reserved")
	private boolean reserved;
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "seatId")
	private Seat seat;
	@ManyToOne(fetch = FetchType.EAGER)
	private Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	private Paymaster paymaster;
	@Column(name = "clientName", length = 128, nullable = false)
	private String clientName;
	@Column(name = "clientSurname", length = 128, nullable = false)
	private String clientSurname;
	@Column(name = "checkDate", nullable = true)
	private Date checkDate;

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public Paymaster getPaymaster() {
		return paymaster;
	}

	public void setPaymaster(Paymaster paymaster) {
		this.paymaster = paymaster;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientSurname() {
		return clientSurname;
	}

	public void setClientSurname(String clientSurname) {
		this.clientSurname = clientSurname;
	}

	public FilmScreeningEvent getEvent() {
		return event;
	}

	public void setEvent(FilmScreeningEvent event) {
		this.event = event;
	}

	public Date getSoldDate() {
		return soldDate;
	}

	public void setSoldDate(Date soldDate) {
		this.soldDate = soldDate;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;

		Ticket ticket = (Ticket) o;

		return event.equals(ticket.event) && seat.equals(ticket.seat);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + event.hashCode();
		result = 31 * result + seat.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "TicketInfo{" + "event=" + event + ", soldDate=" + soldDate + ", seat=" + seat + '}';
	}
}
