package com.generatecsvfiles.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


public class WlcsUtil {

	public static SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf_yyyyMMddhhmmss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static SimpleDateFormat yyyyMMddhhmmss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static SimpleDateFormat yyyyMMddhhmmssS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
	public static final String COMMA = ", ";
	public static final String BLANK_STR = "";

	// Method to retrieve no of pages as per the page count entries
	public static Long getNumbersOfPages(long recordCount, int pageSize) {
		long pages = 1;
		try {
			if ((recordCount % pageSize) == 0) {
				pages = recordCount / pageSize;
			}
			else {
				pages = ((recordCount / pageSize) + 1);
			}
			return pages;
		}
		catch (Exception e) {
		}
		return pages;
	}

	// Method returns date for a given string and the given format
	public static Date getDateFromStringXls(String dateString, String format) throws ParseException {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		date = formatter.parse(dateString);
		return date;
	}

	// Method to check valid MSSQL date
	public static Boolean isValidDate(Date date) {
		Date minDate = null;
		Date maxDate = null;
		try {
			minDate = getDateFromStringXls("01/01/1942", "MM/dd/yyyy");
			maxDate = getDateFromStringXls("12/31/9999", "MM/dd/yyyy");
		}
		catch (Exception e) {
			return false;
		}
		return (date.compareTo(minDate) >= 0 && date.compareTo(maxDate) <= 0);
	}

	public static String indefiniteDateString() {

		Calendar cal = Calendar.getInstance();
		cal.set(9999, 12, 0, 12, 00, 00);
		return sdf_yyyyMMddhhmmss.format(cal.getTime());
	}

	public static void cleanDirectory(String path) {
		try {
			if ((new File(path)).exists()) {

				FileUtils.cleanDirectory(new File(path));
			}
		}
		catch (Exception e) {

			e.printStackTrace();
			System.out.println("Exception occured..while cleaning directory .. " +path);
		}
	}

	public static String appendFileds(String... fields) {

		StringBuilder strbuilder = new StringBuilder().append(BLANK_STR);

		int index = 0;

		for (String field : fields) {

			index = index + 1;

			if (index != 1 && StringUtils.isNotBlank(field)) {

				strbuilder.append(COMMA).append(field.trim());
			}
			else if (StringUtils.isNotBlank(field)) {

				strbuilder.append(field.trim());
			}
		}

		return strbuilder.toString();
	}

	public static String parseDateToyyyyMMddhhmmss(String date) {

		String formattedDate = "";

		try {

			if (StringUtils.isNotBlank(date.trim())) {

				formattedDate = yyyyMMddhhmmss.format(new Date(date.trim())).toString();
			}
		}
		catch (Exception e) {

			formattedDate = date;
		}

		return formattedDate;
	}

	public static String parseDate(String date) {

		String formattedDate = "";

		try {

			if (StringUtils.isNotBlank(date.trim())) {

				formattedDate = yyyyMMddhhmmss.format(yyyyMMddhhmmssS.parse(date)).toString();
			}
		}
		catch (Exception e) {
			
			formattedDate = date;
		}

		return formattedDate;
	}

	public static Map<String, Object> executeCommand(String command, String fromDirectory, String[] args)
			throws Exception {

		Process process = null;
		Map<String, Object> output = new HashMap<String, Object>();

		// check if working directory is provided
		if (StringUtils.isNotBlank(fromDirectory)) {
			process = Runtime.getRuntime().exec(command, args, new File(fromDirectory));
		}
		else {
			process = Runtime.getRuntime().exec(command);
		}

		process.waitFor();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuffer outputBuff = new StringBuffer();
		String line = "";

		while ((line = reader.readLine()) != null) {
			outputBuff.append(line + "\n");
		}
		String commandOutput = outputBuff.toString();
		output.put("OUTPUT", commandOutput);
		output.put("EXITVALUE", process.exitValue());

		return output;
	}

	public static boolean isNumeric(String input) {

		boolean isNumber = false;

		if (StringUtils.isNotBlank(input)) {
			isNumber = input.matches("-?\\d+(\\.\\d+)?");
		}

		return isNumber;
	}
}