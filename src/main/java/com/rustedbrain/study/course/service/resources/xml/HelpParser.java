package com.rustedbrain.study.course.service.resources.xml;

import com.rustedbrain.study.course.model.dto.xml.Content;
import com.rustedbrain.study.course.model.dto.xml.Help;
import com.rustedbrain.study.course.model.dto.xml.Helps;
import com.rustedbrain.study.course.model.dto.xml.Paragraph;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class HelpParser {

	private static final String TITTLE_PREFIX = "<h3>";
	private static final String TITTLE_POSTFIX = "</h3>";
	private static final String PARAGRAPH_TITTLE_PREFIX = "<h4>";
	private static final String PARAGRAPH_TITTLE_POSTFIX = "</h4>";

	public Map<String, String> getHelpTittleTextMap(File file) throws JAXBException {
		Unmarshaller jaxbUnmarshaller = JAXBContext.newInstance(Helps.class).createUnmarshaller();

		Helps helps = (Helps) jaxbUnmarshaller.unmarshal(file);

		Map<String, String> tittleTextMap = new LinkedHashMap<>();
		for (Help help : helps.getHelps()) {
			StringBuilder builder = new StringBuilder();
			for (Content content : help.getContents()) {
				builder.append(TITTLE_PREFIX).append(content.getTittle()).append(TITTLE_POSTFIX);
				for (Paragraph paragraph : content.getParagraphs()) {
					String tittle = paragraph.getTittle();
					if (Objects.nonNull(tittle)) {
						builder.append(PARAGRAPH_TITTLE_PREFIX).append(paragraph.getTittle())
								.append(PARAGRAPH_TITTLE_POSTFIX);
					}
					String text = paragraph.getText();
					builder.append(text);
				}
			}
			tittleTextMap.put(help.getTittle(), builder.toString());
		}
		return tittleTextMap;
	}
}
