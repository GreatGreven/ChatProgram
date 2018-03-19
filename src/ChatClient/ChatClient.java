package ChatClient;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import resauces.*;

public class ChatClient {
	private String ip;
	private int serverPort;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ServerListener listener;

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
			e.printStackTrace();
		}
	}

	protected void send(Message message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Message could not be sent\n" + e.getMessage());
		}
	}

	private class TCPListener extends Thread {
		public void run() {
			while (!Thread.interrupted()) {
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
					JOptionPane.showMessageDialog(null, "TCPLISTENER, CHATCLIENT \n" + e.getMessage());
					try {
						socket.close();
					} catch (IOException e1) {
					}
					Thread.currentThread().interrupt();;
				}
			}
		}
	}
}
