package com.rustedbrain.study.course.service.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class JsonReader {

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static String readAll(final Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject read(final String url) throws IOException, JSONException {
		try (InputStream is = new URL(url).openStream()) {
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName(DEFAULT_CHARSET)));
			final String jsonText = readAll(rd);
			return new JSONObject(jsonText);
		}
	}
}
