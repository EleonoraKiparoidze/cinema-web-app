package com.rustedbrain.study.course.model.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paragraph")
@XmlAccessorType(XmlAccessType.FIELD)
public class Paragraph {

	@XmlElement(name = "tittle")
	private String tittle;
	@XmlElement(name = "text")
	private String text;

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
