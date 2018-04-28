package ChatServer;

import javax.swing.*;

import resources.LogWriter;
import resources.LogReader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Eric Grevillius
 * 
 *         Class that inherits from JPanel and is used as a GUI for seeing all
 *         traffic in the program.
 */
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

	/**
	 * Constructor creates a GUI for seeing the traffic in the server, starting
	 * and stopping.
	 * 
	 * @param controller
	 *            A link to the controller class.
	 */
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

	/**
	 * Creates a panel for the buttons.
	 * 
	 * @return the button-panel
	 */
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

	/**
	 * Sets the final String of text to the text area
	 * 
	 * @param txt
	 *            the final String of text.
	 */
	public static void setText(final String txt) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.setText(txt);
				JScrollBar bar = sp.getVerticalScrollBar();
				bar.setValue(bar.getMaximum() - bar.getVisibleAmount());
			}
		});
	}

	/**
	 * Sets the text of the given object to the text area.
	 * 
	 * @param obj
	 *            the given object.
	 */
	public void setText(Object obj) {
		setText(obj.toString());
	}

	/**
	 * Gives the user options of choosing two time points where the LogUI will
	 * show logged traffic from.
	 */
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
		String logTime = (startTime + ":"+ endTime);
		logReader.read(logTime);
	}

	/**
	 * Adds the final String of text to the text area
	 * 
	 * @param txt
	 *            the final String of text.
	 */
	public void append(final String txt) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(txt);
				log.write(txt);
			}
		});
	}

	/**
	 * Adds the text of the given object to the text area.
	 * 
	 * @param obj
	 *            the given object.
	 */
	public void append(Object obj) {
		append(obj.toString());
	}

	/**
	 * Adds a empty line in the text area.
	 */
	public void println() {
		append("\n");
	}

	/**
	 * Adds the given String to the text area and then goes to a new line.
	 * @param txt The given String.
	 */
	public void println(String txt) {
		append(txt + "\n");
	}

	/**
	 * Adds the given objects toString-method to the text area.
	 * @param obj
	 */
	public void println(Object obj) {
		println(obj.toString());
	}

	/**
	 * Inner class that implements an actionlistener.
	 * 
	 * @author Eric Grevillius
	 *
	 */
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
