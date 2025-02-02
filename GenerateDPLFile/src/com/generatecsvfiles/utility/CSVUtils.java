package com.generatecsvfiles.utility;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVUtils {

	private static final char DEFAULT_SEPARATOR = ',';

	public static void writeLine(Writer w, List<String> values) throws IOException {
		writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	}

	public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
		writeLine(w, values, separators, ' ');
	}

	// https://tools.ietf.org/html/rfc4180
	private static String followCVSformat(String value) {

		// String result = value;
		/*
		 * if (value.contains(",")) { value = value.replace(",", " "); }
		 */

		 if (value.contains("\"")) {
	        	value = value.replace("\"", "\"\"");
	        }
	        
	        if(value.startsWith("\"\"") && value.endsWith("\"\"")) {
	        	value = value.substring(1, value.length()-1);
			}
		
		return value;

	}

	public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

		boolean first = true;

		// default customQuote is empty

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuilder sb = new StringBuilder();

		for (String value : values) {
			if (!first) {
				sb.append(separators);
			}
			if (customQuote == ' ') {
				if (value != null) {
					sb.append(followCVSformat(value));
				}
				else {
					sb.append("null");
				}
			}
			else {
				sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
			}

			first = false;
		}
		sb.append("\n");
		w.append(sb.toString());

	}

}
