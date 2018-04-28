package ChatServer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Class that handles output from Logfile
 * @author Mikael Lindfors
 *
 */


public class LogUI extends JPanel{
	private static final long serialVersionUID = -1598533036400634233L;
	private JTextArea textArea = new JTextArea();
	private JScrollPane jScrollPane = new JScrollPane(textArea);
	
	/**
	 * Constructor that adds an JScrollPane to the panel and sets the size.
	 */
	
	public LogUI() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(400,600));
		this.add(jScrollPane);	
	}
	/**
	 * Method that adds strings line by line to the JTextArea
	 * @param logText String one line from the logfile.
	 */
	public void append(String logText) {
		textArea.append(logText + "\n");
	}
	/**
	 * Method that clears the JTextArea.
	 */
	public void clear() {
		textArea.setText("");
	}
	
	
}