package ChatServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import resources.*;

/**
 * Class that handles the functionality in the server
 * 
 * @author Oskar Engsström Magnusson
 *
 */
public class ChatServer {
	private ServerUI ui;
	private ServerSocket serverSocket;
	private ClientListener listener;
	private HashMap<String, ClientHandler> clientHandlers;
	private UnsentMessages unsentMessages;
	private RunOnThreadN pool;
	private Thread connection;
	private int requestPort;

	/**
	 * Constructor for creating the server
	 * 
	 * @param requestPort
	 *            the port the server runs through
	 * @param nbrOfThreads
	 *            number of threads in the thread-pool
	 * @param listener
	 *            the ClientListener
	 * @param ui
	 *            the server ui
	 */
	public ChatServer(int requestPort, int nbrOfThreads, ClientListener listener, ServerUI ui) {
		this.listener = listener;
		this.unsentMessages = new UnsentMessages();
		this.clientHandlers = new HashMap<String, ClientHandler>();
		this.pool = new RunOnThreadN(nbrOfThreads);
		this.requestPort = requestPort;
		this.ui = ui;
	}

	/**
	 * Method for starting the server by creating a ServerSocket and a
	 * TCPListener
	 */
	protected void startServer() {
		try {
			serverSocket = new ServerSocket(requestPort);
			connection = new TCPListener();
			connection.start();
			pool.start();
			ui.println("Starting Server: " + serverSocket.getInetAddress().getHostAddress() + ":"
					+ serverSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for stopping the server
	 */
	protected void stopServer() {
		try {
			connection.interrupt();
			serverSocket.close();
			pool.stop();
			for (ClientHandler c : clientHandlers.values()){
				c.closeSocket();;
			}
			ui.println("Server closing");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that receives a message and sends it to all receivers. If the user
	 * is not online, the message is added to "Unsent messages"
	 * 
	 * @param message
	 *            a message object
	 */
	protected void respond(Message message) {
		UserList list = message.getReceivers();
		list.addUser(message.getSender());
		for (int i = 0; i < list.numberOfUsers(); i++) {
			if (clientHandlers.containsKey(list.getUser(i).getName())) {
				clientHandlers.get(list.getUser(i).getName()).send(message);
			} else {
				if (unsentMessages.contains(list.getUser(i).getName())) {
					ArrayList<Message> oldList = unsentMessages.get(list.getUser(i).getName());
					oldList.add(message);
					unsentMessages.put(list.getUser(i).getName(), oldList);
				} else {
					ArrayList<Message> newList = new ArrayList<Message>();
					newList.add(message);
					unsentMessages.put(list.getUser(i).getName(), newList);
				}
			}
		}
	}

	/**
	 * Method that receives a UserList and sends it to all users in the list
	 * 
	 * @param listToSend
	 *            a UserList
	 */
	protected void respond(UserList listToSend) {
		for (int i = 0; i < listToSend.numberOfUsers(); i++) {
			clientHandlers.get(listToSend.getUser(i).getName()).send(listToSend);
		}
	}

	/**
	 * Method that returns a list of all users
	 * 
	 * @return list of all users
	 */
	protected UserList getAllUsers() {
		UserList allUsers = new UserList();
		Iterator<String> iter = clientHandlers.keySet().iterator();
		while (iter.hasNext()) {
			allUsers.addUser(new User(iter.next(), null));
		}
		return allUsers;
	}

	/**
	 * Inner class which is listening for users to connect and adds a thread for
	 * each user
	 * 
	 * @author Oskar Engström Magnusson
	 *
	 */
	private class TCPListener extends Thread {
		public void run() {
			while (!Thread.interrupted()) {
				try {
					Socket socket = serverSocket.accept();
					pool.execute(new ClientHandler(socket));

				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Inner class that handles the servers connection with the clients
	 * 
	 * @author Oskar Engström Magnusson
	 *
	 */
	private class ClientHandler implements Runnable {
		private Socket socket;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private User user;

		/**
		 * Constructs a ClientHandler and adds a OutputStream and an InputStream
		 * 
		 * @param socket
		 *            the ServerSocket
		 */
		public ClientHandler(Socket socket) {
			try {
				this.socket = socket;
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Method that receives a UserList and writes it to the OutputStream
		 * 
		 * @param users
		 *            list of users
		 */
		public void send(UserList users) {
			try {
				oos.writeObject(users);
				oos.reset();
				oos.flush();
			} catch (IOException e) {
			}
			if (unsentMessages.contains(user.getName())) {
				ArrayList<Message> recievedList = unsentMessages.get(user.getName());
				for (Message m : recievedList) {
					send(m);
				}
				unsentMessages.remove(user.getName());
			}
		}

		/**
		 * Method that receives a message and writes it to the OutputStream
		 * 
		 * @param message
		 *            a message object
		 */
		public void send(Message message) {
			try {
				message.setReceivedDate();
				ui.println(message);
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) {
			}
		}

		/**
		 * Method for when a user is denied to login, writes a null object to
		 * the OutputStream and closes the socket
		 */
		public void denial() {
			try {
				oos.writeObject(null);
				oos.flush();
				socket.close();
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		/**
		 * Method that reads an object from the InputStream and chooses what to
		 * do depending on which object it is
		 */
		public void run() {
			try {
				Object request = ois.readObject();
				if (request instanceof User) {
					User user = (User) request;
					if (clientHandlers.containsKey(user.getName())) {
						denial();
					} else {
						this.user = user;
						this.user.setConnected(true);
						ui.println(this.user.getName() + " has connected (" + toString() + ")");
						clientHandlers.put(this.user.getName(), this);
						pool.execute(this);
						listener.receive(this.user);
					}
				} else if (request instanceof Message) {
					Message message = (Message) request;
					message.setDeliveredDate();
					pool.execute(this);
					listener.receive(message);
				}
			} catch (Exception e) {
				try {
					disconnect();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		public void disconnect() throws IOException{
			ui.println(user + " has disconnected (" + toString() + ")");
			clientHandlers.remove(user);
			listener.disconnectedUser(user);
		}
		
		public void closeSocket() throws IOException{
			socket.close();
		}

		public String toString() {
			return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
		}
	}
}
