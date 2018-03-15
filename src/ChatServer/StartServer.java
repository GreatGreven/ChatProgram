package ChatServer;

import javax.swing.SwingUtilities;

public class StartServer {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new ServerController(3300, 1);
				} catch (Exception e) {
					System.out.println("Program: " + e);
				}
			}
		});
	}
}
