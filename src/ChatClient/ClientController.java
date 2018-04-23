package ChatClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import resources.Message;
import resources.User;
import resources.UserList;

/**
 * Class that creates a client for the program and controls it.
 * 
 * @author
 *
 */
public class ClientController {
	private LoginUI loginUI;
	private MessageUI messageUI;
	private ChatClient chatClient;
	private JFrame UI;
	private User user;
	private UserList allUsers;
	private UserList contacts;
	private final String filename = "files/localContacts.dat";

	/**
	 * Constructor that opens to the server
	 * 
	 * @param ip
	 *            String with the ip-address to the selected server
	 * 
	 * @param ServerPort
	 *            Port number the server is listening at.
	 */
	protected ClientController(String ip, int serverPort) {
		chatClient = new ChatClient(ip, serverPort, new ServerResponse());
		contacts = new UserList();
		allUsers = new UserList();
		loginUI = new LoginUI(this);
		readContacts();
		showLoginUI();
	}

	/**
	 * Method that show the LogIn screen.
	 */
	private void showLoginUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UI = new JFrame("Login");
				UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				UI.setVisible(true);
				UI.add(loginUI);
				UI.pack();
			}
		});
	}

	/**
	 * Method that show the Message screen.
	 */
	private void showMessageUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UI = new JFrame("Messenger (" + user + ")");
				UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				UI.setVisible(true);
				UI.add(messageUI);
				UI.pack();
				UI.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent arg0) {
						writeContacts();
					}
				});
			}
		});
	}

	/**
	 * Method that reads the UserList from the file.
	 */
	private void readContacts() {
		try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			try {
				contacts = (UserList) ois.readObject();
			} catch (ClassNotFoundException e) {
			}
		} catch (IOException e) {
		}
	}

	/**
	 * Method that writes the contacts to a file.
	 */
	private void writeContacts() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(filename)))) {
			oos.writeObject(contacts);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected User getThisUser() {
		return user;
	}

	protected UserList getContacts() {
		return contacts;
	}

	protected UserList getAllUsers() {
		return allUsers;
	}

	protected void login() {
		String name = loginUI.getName();
		String iconPath = loginUI.getIconPath();
		ImageIcon icon;
		if (iconPath.equals("")) {
			this.user = new User(name);
		} else {
			icon = new ImageIcon(loginUI.getIconPath());
			this.user = new User(name, icon);

		}
		chatClient.login(user);
	}

	/**
	 * Method that sends the message
	 */
	protected void send() {
		String text = messageUI.getText();
		String iconPath = loginUI.getIconPath();
		ImageIcon image;
		if (iconPath.equals("")) {
			image = null;
		} else {
			image = new ImageIcon(messageUI.getImagePath());
		}
		UserList receivers = new UserList();
		ArrayList<String> usernames = messageUI.getReceivers();
		for (int i = 0; i < usernames.size(); i++) {
			if (allUsers.exist(usernames.get(i))) {
				int allUserIndex = allUsers.indexOf(usernames.get(i));
				receivers.addUser(allUsers.getUser(allUserIndex));
			}
		}
		Message message = new Message(user, receivers, text, image);
		chatClient.send(message);
	}

	void addContact(String contact) {
		if (contacts.exist(contact)) {
			JOptionPane.showMessageDialog(null, contact + " already added");
		} else {
			if (allUsers.exist(contact)) {
				contacts.addUser(allUsers.getUser(allUsers.indexOf(contact)));
				allUsers.removeUser(allUsers.getUser(allUsers.indexOf(contact)));

				JOptionPane.showMessageDialog(null, contact + " added");
			} else {
				JOptionPane.showMessageDialog(null, contact + " doesn't exists");
			}
		}
		messageUI.populateContactList();
		messageUI.populateOnlineList();
		messageUI.revalidate();
		messageUI.repaint();
	}

	/**
	 * Inner class that implements a serverlistener that waits to recieve an
	 * message.
	 * 
	 * @author
	 *
	 */
	private class ServerResponse implements ServerListener {
		public void receive(Message message) {
			messageUI.addResponse(message);
		}

		public void receive(UserList userList) {
			allUsers = userList;
			for (int i = 0; i < allUsers.numberOfUsers(); i++) {
				if (contacts.exist(allUsers.getUser(i).getName())) {
					allUsers.removeUser(allUsers.getUser(i));
				}
			}
			if (allUsers.exist(user.getName())) {
				allUsers.removeUser(user);
			}
			if (user.isConnected()) {
				messageUI.populateOnlineList();
				messageUI.revalidate();
				messageUI.repaint();

			} else {
				user.setConnected(true);
				messageUI = new MessageUI(ClientController.this);
				UI.dispose();
				showMessageUI();
			}
		}

		public void accessDenied() {
			JOptionPane.showMessageDialog(null,
					"Access denied! \n \n User already online." + "\n (wait a minute and try again)");
		}
	}

}
