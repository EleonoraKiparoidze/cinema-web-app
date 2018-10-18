package com.rustedbrain.study.course.model.dto.xml;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "content")
@XmlAccessorType(XmlAccessType.FIELD)
public class Content {

	@XmlElement(name = "tittle")
	private String tittle;
	@XmlElementWrapper(name = "paragraphs")
	@XmlElement(name = "paragraph")
	private List<Paragraph> paragraphs;

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}
}
