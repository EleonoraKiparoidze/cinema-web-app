package com.rustedbrain.study.course.model.dto.xml;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "help")
@XmlAccessorType(XmlAccessType.FIELD)
public class Help {

	@XmlElement(name = "tittle")
	private String tittle;
	@XmlElementWrapper(name = "contents")
	@XmlElement(name = "content")
	private List<Content> contents;

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}
}