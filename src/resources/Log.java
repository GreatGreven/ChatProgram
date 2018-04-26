package resources;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

import javax.swing.SwingUtilities;

public class Log {
	private static Log instance;
	private String filename;
	private Calendar c;
	private int year, month, day, hour, minute, second;

	private Log() {
		c = Calendar.getInstance();
	}

	public static Log getInstance() {
		if (instance == null) {
			instance = new Log();
		}
		return instance;
	}

	public void setFileName(String filename) {
		this.filename = filename;
	}

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

	public synchronized void write(String textToFile) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true)))) {
			writer.write("(" + getTime() + ") " + textToFile);
			writer.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
