package ChatServer;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import resouces.*;

public class ServerController {
	private ChatServer chatServer;
	private ServerUI ui;
	
	public ServerController(int requestPort, int nbrOfThreads) {
		ui = new ServerUI(this);
		showServerUI();
		chatServer = new ChatServer(requestPort, nbrOfThreads, new ClientResponse(), ui);
	}
	
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
	
	private class ClientResponse implements ClientListener{
		public void receive(Message message) {
			ui.println(message);
			chatServer.respond(message);
		}

		public void receive(User user) {
			UserList allUsers = chatServer.getAllUsers();
			if (!allUsers.exist(user.getName())){
				allUsers.addUser(user);
			}
			chatServer.respond(allUsers);
		}
		
		public void disconnectedUser(User user) {
			user.setConnected(false);
		}
	}
}
