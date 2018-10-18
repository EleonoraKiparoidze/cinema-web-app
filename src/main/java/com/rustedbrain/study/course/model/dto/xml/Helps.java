package com.rustedbrain.study.course.model.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "helps")
@XmlAccessorType(XmlAccessType.FIELD)
public class Helps {

	@XmlElement(name = "help")
	private List<Help> helps;

	public List<Help> getHelps() {
		return helps;
	}

	public void setHelps(List<Help> helps) {
		this.helps = helps;
	}
}
