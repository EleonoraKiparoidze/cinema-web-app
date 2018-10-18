package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "seat")
public class Seat extends DatabaseEntity implements Comparable<Seat> {

	private static final long serialVersionUID = 6063911824287131532L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rowId")
	private Row row;
	@Column(name = "number", nullable = false)
	private int number;
	@Column(name = "clientCount", nullable = false)
	private int clientCount;
	@Column(name = "price", nullable = false)
	private double price;
	@Column(name = "blockIp")
	private String blockIp;
	@Column(name = "blockDate")
	private Date blockDate;

	public Seat(int number, int clientCount, double price) {
		this.number = number;
		this.clientCount = clientCount;
		this.price = price;
	}

	public Seat() {
	}

	public String getBlockIp() {
		return blockIp;
	}

	public void setBlockIp(String blockIp) {
		this.blockIp = blockIp;
	}

	public Date getBlockDate() {
		return blockDate;
	}

	public void setBlockDate(Date blockDate) {
		this.blockDate = blockDate;
	}

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getClientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;

		Seat seat = (Seat) o;

		return number == seat.number;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + number;
		return result;
	}

	@Override
	public String toString() {
		return "Seat{" + "num:" + number + ", clients:" + clientCount + ", price:" + price + '}';
	}

	@Override
	public int compareTo(Seat o) {
		return Integer.compare(this.number, o.number);
	}
}
