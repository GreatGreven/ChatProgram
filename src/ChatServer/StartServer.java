package ChatServer;

import javax.swing.SwingUtilities;
/**
 * Starts up the Servers thread and sets up the ip and port for the server to connect to
 * @author Matthias Falk
 *
 */
public class StartServer {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new ServerController(3300, 10);
				} catch (Exception e) {
					System.out.println("Program: " + e);
				}
			}
		});
	}
}
