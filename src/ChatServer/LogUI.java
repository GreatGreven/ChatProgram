package ChatServer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogUI extends JPanel{
	private static final long serialVersionUID = -1598533036400634233L;
	private JTextArea textArea = new JTextArea();
	private JScrollPane jScrollPane = new JScrollPane(textArea);
	
	public LogUI() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(400,600));
		this.add(jScrollPane);	
	}
	public void append(String logText) {
		textArea.append(logText + "\n");
	}
	public void clear() {
		textArea.setText("");
	}
	
	
}