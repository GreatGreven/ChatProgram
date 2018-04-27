package resources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import ChatServer.LogUI;

public class LogReader {
	private String filename;
	private LogUI logUI;

	public LogReader(String filename, LogUI logUI) {
		this.filename = filename;
		this.logUI = logUI;
		
	}

	public synchronized void read(String logDate) {
		Calendar timeFrom = Calendar.getInstance();
		Calendar timeTill = Calendar.getInstance();
		Calendar timeInLog = Calendar.getInstance();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
			int[] dateUI = logStringToInt(logDate.split(":"));
			timeFrom.set(dateUI[0], dateUI[1], dateUI[2], dateUI[3], dateUI[4]);
			timeTill.set(dateUI[5], dateUI[6], dateUI[7], dateUI[8], dateUI[9]);
			String line = reader.readLine();
			while (line != null) {
				int[] timeFromLog = logStringToInt(parseFromLog(line));
				timeInLog.set(timeFromLog[0], timeFromLog[1], timeFromLog[2], timeFromLog[3], timeFromLog[4]);
				if (timeFrom.before(timeInLog) && timeTill.after(timeInLog)) {
					logUI.append(line.toString());
				}
				line = reader.readLine();
			}
		} catch (
		Exception e) {
			System.err.println(e);
		}
	}

	private String[] parseFromLog(String line) {
		String[] parts = new String[5];
		parts[0] = line.substring(1, 5);
		parts[1] = line.substring(6, 8);
		parts[2] = line.substring(9, 11);
		parts[3] = line.substring(12, 14);
		parts[4] = line.substring(15, 17);
		return parts;
	}

	/**
	 * Method that takes format:
	 * fromYear:fromMonth:fromDay:fromHour:fromMinute:tillYear:tillMonth:tillDay:tillHour:tillMinute
	 * and convert every slot to int and store it in an int array.
	 * @param logDate String with format: fromYear:fromMonth:fromDay:fromHour:fromMinute:tillYear:tillMonth:tillDay:tillHour:tillMinute
	 */
	public int[] logStringToInt(String[] logDate) {
		int[] parts = new int[logDate.length];
		for (int index = 0; index < logDate.length; index++) {
			parts[index] = Integer.parseInt(logDate[index]);
		}
		return parts;
	}
}

