package ChatServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import resources.*;

public class ChatServer {
	private ServerUI ui;
	private ServerSocket serverSocket;
	private ClientListener listener;
	private HashMap<String, ClientHandler> clientHandlers;
	private UnsentMessages unsentMessages;
	private RunOnThreadN pool;
	private Thread connection;
	private int requestPort;
	private LinkedList<ClientHandler> clientHandlerList = new LinkedList<ClientHandler>();

	public ChatServer(int requestPort, int nbrOfThreads, ClientListener listener, ServerUI ui) {
		this.listener = listener;
		this.unsentMessages = new UnsentMessages();
		this.clientHandlers = new HashMap<String, ClientHandler>();
		this.pool = new RunOnThreadN(nbrOfThreads);
		this.requestPort = requestPort;
		this.ui = ui;
	}

	protected void startServer() {
		try {
			serverSocket = new ServerSocket(requestPort);
			connection = new TCPListener();
			connection.start();
//			pool.start();
			ui.println("Starting Server: " + serverSocket.getInetAddress().getHostAddress() + ":"
					+ serverSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void stopServer() {
		try {
			connection.interrupt();
			serverSocket.close();
			pool.stop();
			clientHandlers.clear();
			ui.println("Server closing");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void respond(Message message) {
		UserList list = message.getReceivers();
		list.addUser(message.getSender());
		for (int i = 0; i < list.numberOfUsers(); i++) {
			clientHandlers.get(list.getUser(i)).send(message);
		}
	}

	protected void respond(UserList listToSend) {
		for (int i = 0; i < listToSend.numberOfUsers(); i++) {
				clientHandlers.get(listToSend.getUser(i).getName()).send(listToSend);
		}
	}
	
	protected UserList getAllUsers() {
		UserList allUsers = new UserList();
		Iterator<String> iter = clientHandlers.keySet().iterator();
		while(iter.hasNext()) {
			allUsers.addUser(new User(iter.next(),null));
		}
		return allUsers;
	}

	private class TCPListener extends Thread {
		public void run() {
			while (!Thread.interrupted()) {
				try {
					Socket socket = serverSocket.accept();
//					pool.execute(new ClientHandler(socket));
					clientHandlerList.add(new ClientHandler(socket));
					clientHandlerList.getLast().start();
					
				} catch (IOException e) {
				}
			}
		}
	}

	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private User user;

		public ClientHandler(Socket socket) {
			try {
				this.socket = socket;
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void send(UserList users) {
			try {
				System.out.println(users);
				oos.writeObject(users);
				oos.reset();
				oos.flush();
			} catch (IOException e) {
			}
		}

		public void send(Message message) {
			try {
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) {
				if (unsentMessages.contains(user.getName())) {
					ArrayList<Message> list = unsentMessages.get(user.getName());
					list.add(message);
					unsentMessages.put(user.getName(), list);
				} else {
					ArrayList<Message> newList = new ArrayList<Message>();
					newList.add(message);
					unsentMessages.put(user.getName(), newList);
				}
			}
		}

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

		public void run() {
			while (!Thread.interrupted()) {
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
							// pool.execute(this);
							listener.receive(this.user);
						}
					} else if (request instanceof Message) {
						Message message = (Message) request;
						// pool.execute(this);
						listener.receive(message);
					}
				} catch (SocketException e) {
				} catch (Exception e) {
					try {
						ui.println(user + " has disconnected (" + toString() + ")");
						socket.close();
						clientHandlers.remove(user);
						listener.disconnectedUser(user);

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		public String toString() {
			return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
		}
	}
}
