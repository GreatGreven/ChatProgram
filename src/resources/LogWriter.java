package resources;

import java.io.*;
import java.util.Calendar;
/**
 * Class that handles log-output and write it to the harddrive.
 * @author Mikael Lindfors
 *
 */

public class LogWriter {
	private static LogWriter instance;
	private String filename;
	private Calendar c;
	private int year, month, day, hour, minute, second;

	/**
	 * Constructor that fetch an instance from Calendar.
	 */
	private LogWriter() {
		c = Calendar.getInstance();
	}

	/**
	 * Method that returns an instance of LogWriter (singleton)
	 * @return LogWriter instance.
	 */
	public static LogWriter getInstance() {
		if (instance == null) {
			instance = new LogWriter();
		}
		return instance;
	}

	/**
	 * Method that sets the fileName for the logfile.
	 * @param filename String with the filename.
	 */
	public void setFileName(String filename) {
		this.filename = filename;
	}

	/**
	 * Method that fetch the time from the Calendar object and format it in a correct way
	 * @return String with the current time in format: year-month-day hour:minute:second
	 * 
	 */ 
	private String getTime() {
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DATE);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		second = c.get(Calendar.SECOND);
		String time = year + "-" + (month < 10 ? ("0" + month) : (month)) + "-" + (day < 10 ? ("0" + day) : (day)) + " "
				+ (hour < 10 ? ("0" + hour) : (hour)) + ":" + (minute < 10 ? ("0" + minute) : (minute)) + ":"
				+ (second < 10 ? ("0" + second) : (second));
		return time;
	}
/**
 * Method that writes a String line to the logfile together with a correct timestamp.
 * @param textToFile String with one line of text.
 */
	public synchronized void write(String textToFile) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true)))) {
			writer.write("(" + getTime() + ") " + textToFile);
			writer.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
