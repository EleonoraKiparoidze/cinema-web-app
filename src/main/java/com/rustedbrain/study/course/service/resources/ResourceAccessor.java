package com.rustedbrain.study.course.service.resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.rustedbrain.study.course.model.exception.ResourceException;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.service.resources.xml.CinemaHallParser;
import com.rustedbrain.study.course.service.resources.xml.HelpParser;

@Component
public class ResourceAccessor {

	private static final Logger LOG = Logger.getLogger(ResourceAccessor.class.getName());

	public static final String CREATED_CINEMA_HALL_DIR_NAME = "." + File.separator + "halls" + File.separator;

	private static final String IP_GEO_DATABASE_RESOURCE = "GeoLiteCity.dat";
	private static final String HELP_FILE_NAME = "help.xml";

	private static final String CINEMA_HALL_FILE_NAME = "cinemaHall.xml";

	public Map<String, String> getHelpTittleTextMap()
			throws ParserConfigurationException, IOException, SAXException, ResourceException {
		try {
			return new HelpParser().getHelpTittleTextMap(getResourceFile(HELP_FILE_NAME));
		} catch (JAXBException e) {
			LOG.log(Level.WARNING, "Error occurred during getting info for help page", e);
			throw new ResourceException(e);
		}
	}

	public Map<Integer, List<Integer>> getDefaultCinemaHallSeatCoordinateMultiMap()
			throws ParserConfigurationException, IOException, SAXException, ResourceException {
		try {

			return new CinemaHallParser().getCinemaHallSeatCoordinateMap(getResourceFile(CINEMA_HALL_FILE_NAME));

		} catch (JAXBException e) {
			LOG.log(Level.WARNING, "Error occurred during getting info for cinema hall constructor page", e);
			throw new ResourceException(e);
		}
	}

	public Map<Integer, List<Integer>> getCinemaHallSeatCoordinateMap(CinemaHall cinemaHall)
			throws ParserConfigurationException, IOException, SAXException, ResourceException {
		try {
			String cinemaHallId = String.valueOf(cinemaHall.getId());
			Path createdHallsPath = Paths.get(CREATED_CINEMA_HALL_DIR_NAME + cinemaHallId + ".xml");
			File createdHallFile = createdHallsPath.toFile();

			if (!createdHallFile.exists()) {
				return getDefaultCinemaHallSeatCoordinateMultiMap();
			} else {
				return new CinemaHallParser().getCinemaHallSeatCoordinateMap(createdHallFile);
			}
		} catch (JAXBException e) {
			LOG.log(Level.WARNING, "Error occurred during getting info for cinema hall constructor page", e);
			throw new ResourceException(e);
		}
	}

	public File getIPGeoDatabaseFile() {
		return new File(
				Objects.requireNonNull(getClass().getClassLoader().getResource(IP_GEO_DATABASE_RESOURCE)).getFile());
	}

	private File getResourceFile(String fileName) {
		return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile());
	}

	public void setCinemaHallSeatMap(long cinemeHallId, Map<Integer, List<Integer>> cinemaHallSeatCoordinateMultiMap) throws IOException, ResourceException {
		try {
			new CinemaHallParser().setCinemaHallSeatMap(cinemeHallId, cinemaHallSeatCoordinateMultiMap);
		} catch (JAXBException e) {
				LOG.log(Level.WARNING, "Error occurred during getting info for cinema hall constructor page", e);
				throw new ResourceException(e);
		}
	}
}
