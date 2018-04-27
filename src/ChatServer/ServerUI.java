package ChatServer;

import javax.swing.*;

//import resources.Date;
import resources.LogWriter;
import resources.LogReader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerUI extends JPanel {
	private static final long serialVersionUID = -2041693529144462758L;
	private ServerController controller;
	private static JTextArea textArea;
	private static JScrollPane sp;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnLog;
	private LogWriter log;
	private LogUI logUI;
	private JFrame logFrame;
	private JButton btnClose;
	private final String fileName = "files/serverLog.txt";

	public ServerUI(ServerController controller) {
		this.controller = controller;
		log = LogWriter.getInstance();
		log.setFileName(fileName);
		logUI = new LogUI();
		int width = 400;
		int height = width;
		Dimension windowSize = new Dimension(width, height);
		this.setPreferredSize(windowSize);
		this.setLayout(new BorderLayout());

		textArea = new JTextArea();
		textArea.setEditable(false);
		sp = new JScrollPane(textArea);
		btnClose = new JButton("Close");
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
		btnClose.addActionListener(aListener);
		panel.add(btnStart, BorderLayout.SOUTH);
		panel.add(btnStop, BorderLayout.SOUTH);
		panel.add(btnLog, BorderLayout.SOUTH);
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
		String startTime = JOptionPane.showInputDialog("Enter start time (year:month:date:hour:minute)");
		String endTime = JOptionPane.showInputDialog("Enter end time (year:month:date:hour:minute)");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				logFrame = new JFrame("LogUI");
				JPanel btnPanel = new JPanel();
				btnPanel.add(btnClose);
				logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				logFrame.setVisible(true);
				logFrame.add(logUI);
				logFrame.pack();
				logFrame.add(btnPanel, BorderLayout.SOUTH);
			}
		});
		LogReader logReader = new LogReader(fileName, logUI);
		logReader.read(startTime + ":" + endTime);
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
				checkLog();
			}
			if (e.getSource() == btnClose) {
				logUI.clear();
				logFrame.dispose();
			}
		}
	}

}
