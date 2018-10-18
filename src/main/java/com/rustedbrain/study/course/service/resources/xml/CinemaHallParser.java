package com.rustedbrain.study.course.service.resources.xml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.rustedbrain.study.course.model.dto.xml.Coordinate;
import com.rustedbrain.study.course.model.dto.xml.Row;
import com.rustedbrain.study.course.model.dto.xml.Rows;
import com.rustedbrain.study.course.model.dto.xml.Seat;
import com.rustedbrain.study.course.service.resources.ResourceAccessor;

public class CinemaHallParser {

	public Map<Integer, List<Integer>> getCinemaHallSeatCoordinateMap(File file) throws JAXBException {
		Unmarshaller jaUnmarshaller = JAXBContext.newInstance(Rows.class).createUnmarshaller();
		Rows rows = (Rows) jaUnmarshaller.unmarshal(file);
		Map<Integer, List<Integer>> cinemaHallSeatMap = new TreeMap<>();

		for (Row row : rows.getRows()) {
			List<Integer> yList = new ArrayList<>();
			int x = 0;
			for (Seat seat : row.getSeats()) {
				yList.add(seat.getCoordinate().getY());
				x = seat.getCoordinate().getX();
			}
			cinemaHallSeatMap.put(x, yList);
		}
		return cinemaHallSeatMap;
	}

	public void setCinemaHallSeatMap(long cinemaHallId, Map<Integer, List<Integer>> cinemaHallSeatCoordinateMultiMap)
			throws IOException, JAXBException {

		List<Row> rowsList = new LinkedList<>();
		cinemaHallSeatCoordinateMultiMap.entrySet().stream().forEach(enty -> {
			Row row = new Row();
			List<Seat> seats = new LinkedList<>();
			enty.getValue().forEach(value -> {
				Seat seat = new Seat();
				seat.setCoordinate(new Coordinate(enty.getKey(), value));
				seats.add(seat);
			});
			row.setSeats(seats);
			rowsList.add(row);
		});
		Rows rows = new Rows();
		rows.setRows(rowsList);

		createHallSeatsMapXMLFile(cinemaHallId, rows);

	}

	private void createHallSeatsMapXMLFile(long cinemaHallId, Rows rows) throws IOException, JAXBException {
		Path hallsPath = Paths.get(ResourceAccessor.CREATED_CINEMA_HALL_DIR_NAME);
		boolean folderExists = hallsPath.toFile().exists();
		if (!folderExists) {
			hallsPath.toFile().mkdirs();
		}
		Path path = Paths.get(ResourceAccessor.CREATED_CINEMA_HALL_DIR_NAME + cinemaHallId + ".xml");
		File file = path.toAbsolutePath().toFile();
		boolean fileExists = file.exists();
		if (!fileExists) {
			fileExists = file.createNewFile();
		}
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Rows.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(rows, file);
	}

}
