package resources;

import java.io.*;
import java.text.SimpleDateFormat;

import javax.swing.SwingUtilities;

public class Log {
	private static Log instance;
	private String filename;
	private Date date;
	
	private Log() {
	}
	
	public static Log getInstance(){
		if (instance == null){
			instance = new Log();
		}
		return instance;	
	}
	
	public void setFileName(String filename){
		this.filename = filename;
	}
	
	public synchronized void write(String textToFile){
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename,true)))){
			date = new Date();
			writer.write("(" + date + ") " +textToFile  );
			writer.newLine();
			writer.flush();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				Log log = Log.getInstance();
				log.setFileName("files/logging.txt");
				Thread t1 = new Thread(new Runnable(){
					public void run() {
						log.write("Jag skapades f�rst");
						for (int i = 0; i <= 5; i++){
							log.write("A" + i);
						}
						log.write("���");
					}
				});
				Thread t2 = new Thread(new Runnable(){
					public void run() {
						log.write("Jag kom senare!");
						for (int i = 0; i <= 5; i++){
							log.write("B" + i);
						}
						log.write("?!#");
					}
				});
				Thread t3 = new Thread(new Runnable(){
					public void run() {
						log.write("Jag kommer sist");
						for (int i = 0; i <= 5; i++){
							log.write("C" + i);
						}
					}
				});
				t1.start();
				t2.start();
				t3.start();
			}
			
		});
	}
}
