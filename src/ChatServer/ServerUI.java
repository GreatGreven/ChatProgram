package ChatServer;

import javax.swing.*;

import resources.Date;
import resources.Log;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerUI extends JPanel {
	private ServerController controller;
	private static JTextArea textArea;
	private static JScrollPane sp;
	private JButton btnStart;
	private JButton btnStop;
	private Log log;
	private Date date;

	public ServerUI(ServerController controller) {
		this.controller = controller;
		this.date = new Date();
		log = Log.getInstance();
		log.setFileName("files/serverLog.txt");
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
	
	private JPanel buttonPanel(){
		JPanel panel = new JPanel(new GridLayout(1,2));
		btnStart = new JButton("Start");
		btnStop = new JButton("Stop");
		AL aListener = new AL();
		btnStart.addActionListener(aListener);
		btnStop.addActionListener(aListener);
		panel.add(btnStart, BorderLayout.SOUTH);
		panel.add(btnStop,BorderLayout.SOUTH);
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
