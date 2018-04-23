package ChatClient; 
import javax.swing.SwingUtilities; 

public class StartClient {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new ClientController("localhost",3300);
				} catch(Exception e) {
					System.out.println("Program: "+e);
				}
			}
		});
	}
}
