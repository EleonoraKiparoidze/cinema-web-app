package com.rustedbrain.study.course.model.dto.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class Row {

	@XmlElementWrapper(name = "seats")
	@XmlElement(name = "seat")
	private List<Seat> seats;

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	@Override
	public String toString() {
		return "Row [seats=" + seats + "]";
	}

}
