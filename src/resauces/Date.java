package resauces;

import java.util.Calendar;

public class Date {
	private long year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private long ms;
	
	public Date() {
		this.year = Calendar.YEAR;
		this.month = Calendar.MONTH;
		this.day = Calendar.DAY_OF_MONTH;
		this.hour = Calendar.HOUR_OF_DAY;
		this.minute = Calendar.MINUTE;
		this.second = Calendar.SECOND;
		this.ms = Calendar.MILLISECOND;
	}
	
	public long getYear(){
		return year;
	}
	
	public int getMonth(){
		return month;
	}

	public int getDay(){
		return day;
	}
	
	public int getHour(){
		return hour;
	}
	
	public int getMinute(){
		return minute;
	}
	
	public int getSecond(){
		return second;
	}
	
	public long getMillisecond(){
		return ms;
	}
	
	public String toString(){
		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + ":" + ms;
	}
}
