package resources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import ChatServer.LogUI;

/**
 * Class that handles reading from Logfile
 * 
 * @author Mikael Lindfors
 *
 */
public class LogReader {
	private String filename;
	private LogUI logUI;

	/**
	 * Constructor that receives filename and LogUI object
	 * 
	 * @param filename String filename of the logfile.
	 * @param logUI LogUI object for displaying text in the LogUI's JTextArea
	 */
	public LogReader(String filename, LogUI logUI) {
		this.filename = filename;
		this.logUI = logUI;

	}

	/**
	 * Method that takes a String in format
	 * fromYear:fromMonth:fromDay:fromHour:fromMinute:tillYear:tillMonth:tillDay:tillHour:tillMinute
	 * and splits it to seperate parts and adds it to Calendarobjects that will be
	 * compared to a timestamp (also added to a Calendarobject) from the logfile. If
	 * the timestamp from Logfile is between timeFrom and timeTill, log-output will
	 * be send to the LogUI's JTextArea.
	 * 
	 * @param logTime String with timestamps
	 */
	public synchronized void read(String logTime) {
		Calendar timeFrom = Calendar.getInstance();
		Calendar timeTill = Calendar.getInstance();
		Calendar timeInLog = Calendar.getInstance();

				try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
			int[] dateUI = logStringToInt(logTime.split(":"));
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
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * Method that fetch the timestamp from a String line (from the logfile).
	 * 
	 * @param line String with format (year-month-day hour:minute:second)
	 * @return return a String array with year,month,day,hour,minute,second as
	 *         separate parts.
	 */
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
	 * Method that takes a String array with both fromTime and tillTime and converts
	 * it to an int-array.
	 * 
	 * @param logDate String array with format [0] = fromYear, [1] = fromMonth, [2]
	 *            = fromDay, [3] = fromHour, [4] = fromMinute, [5] = tillYear, [6] =
	 *            tillMonth, [7] = tillDay, [8] = tillHour, [9] = tillMinute.
	 * 
	 */
	public int[] logStringToInt(String[] logDate) {
		int[] parts = new int[logDate.length];
		for (int index = 0; index < logDate.length; index++) {
			parts[index] = Integer.parseInt(logDate[index]);
		}
		return parts;
	}
}
