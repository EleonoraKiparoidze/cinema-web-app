package com.rustedbrain.study.course.model.dto.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rows")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rows {

	@XmlElement(name = "row")
	private List<Row> rows;

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "Rows [rows=" + rows + "]";
	}
}
