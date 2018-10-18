package com.rustedbrain.study.course.model.geolocation;

import java.io.Serializable;

public class Point implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6499642264847634006L;
	private double longitude;
	private double latitude;

	public Point(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Point point = (Point) o;

		return Double.compare(point.longitude, longitude) == 0 && Double.compare(point.latitude, latitude) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(longitude);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Point{" + "longitude=" + longitude + ", latitude=" + latitude + '}';
	}
}
