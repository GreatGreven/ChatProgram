package ChatServer;

import javax.swing.*;

//import resources.Date;
import resources.Log;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ServerUI extends JPanel {
	private static final long serialVersionUID = -2041693529144462758L;
	private ServerController controller;
	private static JTextArea textArea;
	private static JScrollPane sp;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnLog;
	private Log log;
	private final String fileName = "files/serverLog.txt";

	public ServerUI(ServerController controller) {
		this.controller = controller;
		log = Log.getInstance();
		log.setFileName(fileName);
		int width = 400;
		int height = width;
		Dimension windowSize = new Dimension(width, height);
		this.setPreferredSize(windowSize);
		this.setLayout(new BorderLayout());

		textArea = new JTextArea();
		textArea.setEditable(false);
		sp = new JScrollPane(textArea);
		this.add(sp, BorderLayout.CENTER);
		this.add(buttonPanel(), BorderLayout.SOUTH);
	}

	private JPanel buttonPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 3));
		btnStart = new JButton("Start");
		btnStop = new JButton("Stop");
		btnLog = new JButton("Check log");
		AL aListener = new AL();
		btnStart.addActionListener(aListener);
		btnStop.addActionListener(aListener);
		btnLog.addActionListener(aListener);
		panel.add(btnStart, BorderLayout.SOUTH);
		panel.add(btnStop, BorderLayout.SOUTH);
		return panel;
	}

	public static void setText(final String txt) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.setText(txt);
				JScrollBar bar = sp.getVerticalScrollBar();
				bar.setValue(bar.getMaximum() - bar.getVisibleAmount());
			}
		});
	}

	public void setText(Object obj) {
		setText(obj.toString());
	}

	public void checkLog() {
		
		String startTime = JOptionPane.showInputDialog("Enter start time (year:month:date:hour:minute:second)");
		String endTime = JOptionPane.showInputDialog("Enter end time (year:month:date:hour:minute:second)");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
//			Stringreader.readLine();
			
		} catch (IOException e) { System.out.println(e.getMessage());
		}
	}

	public void append(final String txt) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(txt);
				log.write(txt);
			}
		});
	}

	public void append(Object obj) {
		append(obj.toString());
	}

	public void println() {
		append("\n");
	}

	public void println(String txt) {
		append(txt + "\n");
	}

	public void println(Object obj) {
		println(obj.toString());
	}

	private class AL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnStart) {
				controller.startServer();
			}
			if (e.getSource() == btnStop) {
				controller.stopServer();
			}
			if (e.getSource() == btnLog) {

			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("TestUI");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.add(new ServerUI(null));
				frame.pack();
			}

		});
	}

}
