package ChatServer;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import resources.*;

/**
 * Class for controlling the server
 * @author Oskar Engström Magnusson
 *
 */
public class ServerController {
	private ChatServer chatServer;
	private ServerUI ui;
	
	/**
	 * Constructor that creates the server UI and a chatServer-object
	 * @param requestPort the port the server runs through
	 * @param nbrOfThreads number of threads in the thread-pool
	 */
	public ServerController(int requestPort, int nbrOfThreads) {
		ui = new ServerUI(this);
		showServerUI();
		chatServer = new ChatServer(requestPort, nbrOfThreads, new ClientResponse(), ui);
	}
	
	/**
	 * Method for showing the server-UI
	 */
	private void showServerUI(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				JFrame frame = new JFrame("Server");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.add(ui);
				frame.pack();
			}
		});
	}
	
	protected void startServer(){
		chatServer.startServer();
	}
	
	protected void stopServer(){
		chatServer.stopServer();
	}
	
	/**
	 * Inner class that listens to clients and implements ClientListener
	 * @author Oskar Engström Magnsson
	 *
	 */
	private class ClientResponse implements ClientListener{
		
		/**
		 * Method that receives a message from a client
		 */
		public void receive(Message message) {
			chatServer.respond(message);
		}

		/**
		 * Method that receives a user from a client
		 */
		public void receive(User user) {
			UserList allUsers = chatServer.getAllUsers();
			if (!allUsers.exist(user.getName())){
				allUsers.addUser(user);
			}
			chatServer.respond(allUsers);
		}
		
		/**
		 * Method for when a user has disconnected
		 */
		public void disconnectedUser(User user) {
			user.setConnected(false);
			UserList allUsers = chatServer.getAllUsers();
			chatServer.respond(allUsers);
		}
	}
}
