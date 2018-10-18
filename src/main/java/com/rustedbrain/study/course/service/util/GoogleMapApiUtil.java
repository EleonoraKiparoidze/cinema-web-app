package com.rustedbrain.study.course.service.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.rustedbrain.study.course.model.geolocation.Point;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.sqrt;

public class GoogleMapApiUtil {

	private static final String GOOGLE_API_ADDRESS = "http://maps.googleapis.com/maps/api/geocode/json";
	private static final int EARTH_RADIUS_KILOMETERS = 6371; // Радиус Земли

	private static String encodeParams(final Map<String, String> params) {
		return Joiner.on('&').join(// получаем значение вида key1=value1&key2=value2...
				params.entrySet().stream().map(input -> {
					try {// соответствии со стандартом
							// HTML 4.01
						return input.getKey() + '=' + // получаем значение вида key=value
						URLEncoder.encode(input.getValue(), "utf-8");// кодирует строку в
					} catch (final UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				}).collect(Collectors.toList()));
	}

	public static Point getCoordinatesByAddress(String address) throws IOException {
		final Map<String, String> params = Maps.newHashMap();
		params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
		params.put("address", address);// адрес, который нужно геокодировать
		final String url = GOOGLE_API_ADDRESS + '?' + encodeParams(params);// генерируем путь с параметрами
		final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
		// как правило наиболее подходящий ответ первый и данные о координатах можно
		// получить по пути
		// //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
		JSONObject location = response.getJSONArray("results").getJSONObject(0);
		location = location.getJSONObject("geometry");
		location = location.getJSONObject("location");
		return new Point(location.getDouble("lng"), location.getDouble("lat"));
	}

	public static String getAddressByCoordinates(double longitude, double latitude) throws IOException {
		final Map<String, String> params = Maps.newHashMap();
		params.put("language", "ru");// язык данных, на котором мы хотим получить
		params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
		// текстовое значение широты/долготы, для которого следует получить ближайший
		// понятный человеку адрес, долгота и
		// широта разделяется запятой, берем из предыдущего примера
		params.put("latlng", longitude + "," + latitude);
		final String url = GOOGLE_API_ADDRESS + '?' + encodeParams(params);// генерируем путь с параметрами
		final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
		// как правило, наиболее подходящий ответ первый и данные об адресе можно
		// получить по пути
		// //results[0]/formatted_address
		final JSONObject location = response.getJSONArray("results").getJSONObject(0);
		return location.getString("formatted_address");// итоговый адрес
	}

	/**
	 * Returns distance between two addresses using google geocoding api.
	 *
	 * @param address1 the first address
	 * @param address2 the second address
	 * @return distance between two addresses in kilometers
	 */
	public static double getDistanceBetweenAddresses(String address1, String address2) throws IOException {
		final Point subwayStationPoint = getCoordinatesByAddress(address1);
		final Point addressPoint = getCoordinatesByAddress(address2);
		// Рассчитываем расстояние между точками
		final double dlng = Math.toRadians(subwayStationPoint.getLongitude() - addressPoint.getLongitude());
		final double dlat = Math.toRadians(subwayStationPoint.getLatitude() - addressPoint.getLatitude());
		final double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) + Math.cos(Math.toRadians(addressPoint.getLatitude()))
				* Math.cos(Math.toRadians(subwayStationPoint.getLatitude())) * Math.sin(dlng / 2) * Math.sin(dlng / 2);
		final double c = 2 * Math.atan2(sqrt(a), sqrt(1 - a));
		return c * EARTH_RADIUS_KILOMETERS; // получаем расстояние в километрах
	}
}
