package ChatClient;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import resources.*;

/**
 * Class that opens a Socket to our Server and starts the ServerListener that
 * will recieve Messages and a User list.
 * 
 * @author Martin Gyllstrom
 *
 */

public class ChatClient {
	private String ip;
	private int serverPort;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ServerListener listener;

	/**
	 * Constructor that opens to the server and starts an ObjectInputStream to the
	 * socket and a serverlistener.
	 * 
	 * @param ip
	 *            String with the ip-address to the selected server
	 * @param serverPort
	 *            Port number the server is listening at.
	 * @param listener
	 *            that adds a listener to the MessageList.
	 */

	protected ChatClient(String ip, int serverPort, ServerListener listener) {
		this.ip = ip;
		this.serverPort = serverPort;
		this.listener = listener;
	}

	protected void login(User user) {
		try {
			this.socket = new Socket(ip, serverPort);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			oos.writeObject(user);
			oos.flush();
			new TCPListener().start();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Can not connect to server \n" + e.getMessage());
		}
	}

	/**
	 * Method that sends a message to the chosen port.
	 * 
	 * @param message
	 *            sending message to selected server.
	 */
	protected void send(Message message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Message could not be sent\n" + e.getMessage());
		}
	}

	/**
	 * Inner class that creates a thread so the client recieves an Object from the
	 * server. When an Object is recieved it will be sent to the UserList.
	 * 
	 * @author Martin Gyllstrom
	 *
	 */

	private class TCPListener extends Thread {
		public void run() {
			while (!socket.isClosed()) {
				try {
					Object response = ois.readObject();
					System.out.println(response);
					if (response instanceof UserList) {
						UserList list = (UserList) response;
						listener.receive(list);
					} else if (response instanceof Message) {
						Message message = (Message) response;
						listener.receive(message);
					} else if (response == null) {
						listener.accessDenied();
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "TCPLISTENER interrupted" + e.getMessage());
					try {
						socket.close();
					} catch (IOException e1) {
					}

				}
			}
		}
	}
}
