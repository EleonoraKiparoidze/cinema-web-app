package com.rustedbrain.study.course.model.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "seat")
@XmlAccessorType(XmlAccessType.FIELD)
public class Seat {
	@XmlElement(name = "coordinate")
	private Coordinate coordinate;
	
	@XmlElement(name = "number")
	private String number;

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Seat [coordinate=" + coordinate + "]";
	}
	
}
