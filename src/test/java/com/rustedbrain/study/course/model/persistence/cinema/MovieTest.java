package com.rustedbrain.study.course.model.persistence.cinema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;

public class MovieTest {

	private static Movie movie1;
	private static Movie movie2;
	private static Movie movie3;

	@BeforeClass
	public static void setUpDataBeforeClass() {
		movie1 = new Movie();
		movie2 = new Movie();
		movie1.setId(1);
		movie1.setOriginalName("OriginalName");
		movie1.setLocalizedName("LocalizedNameOne");
		movie1.setReleaseDate(Date.valueOf(LocalDate.of(2017, 3, 3)));

		movie2.setId(1);
		movie2.setOriginalName("OriginalName");
		movie1.setLocalizedName("LocalizedNameTwo");
		movie2.setReleaseDate(Date.valueOf(LocalDate.of(2017, 3, 3)));

		movie3 = new Movie();
		movie3.setId(6);
		movie3.setOriginalName("Saw");
		movie3.setLocalizedName("Saw");
		movie3.setReleaseDate(Date.valueOf(LocalDate.of(2017, 10, 31)));
	}

	@Test
	public void testSymmetryEquals() {
		assertTrue(movie1.equals(movie2) && movie2.equals(movie1));
	}

	@Test
	public void testNotEquals() {
		assertFalse(movie2.equals(movie3));
	}

	@Test
	public void testEqualsConsistency() {
		assertEquals(movie1.equals(movie2), movie1.equals(movie2));
		movie1.setOriginalName("TestName");
		assertFalse(movie1.equals(movie2));
	}

	@Test
	public void testEqualsComparisonOfNull() {
		assertEquals(false, movie1.equals(null));
	}

	@Test
	public void testEqualsReflectivity() {
		assertTrue(movie1.equals(movie1));
	}

	@Test
	public void testHashCode() {
		assertTrue(movie1.hashCode() == movie2.hashCode());
	}
}
