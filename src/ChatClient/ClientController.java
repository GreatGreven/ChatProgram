package ChatClient;

import java.io.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import resources.Message;
import resources.User;
import resources.UserList;

public class ClientController {
	private LoginUI loginUI;
	private MessageUI messageUI;
	private ChatClient chatClient;
	private JFrame UI;
	private User user;
	private UserList allUsers;
	private UserList contacts;
	private final String filename = "files/localContacts.dat";

	protected ClientController(String ip, int serverPort) {
		chatClient = new ChatClient(ip, serverPort, new ServerResponse());
		contacts = new UserList();
		allUsers = new UserList();
		loginUI = new LoginUI(this);
		messageUI = new MessageUI(this);

		showLoginUI();
	}

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

	private void showMessageUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UI = new JFrame("Messenger (" + user + ")");
				UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				UI.setVisible(true);
				UI.add(messageUI);
				UI.pack();
			}
		});
	}

	private void readContacts() {
		try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			try {
				contacts = (UserList) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
		}
	}

	private void writeContacts() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(filename)))) {
			oos.writeObject(contacts);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			icon = null;
		} else {
			icon = new ImageIcon(loginUI.getIconPath());
		}
		User user = new User(name, icon);
		chatClient.login(user);
		this.user = user;
	}

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
			JOptionPane.showMessageDialog(null, contact + " already exists");
		} else {
			if (allUsers.exist(contact)) {
				contacts.addUser(allUsers.getUser(allUsers.indexOf(contact)));
				JOptionPane.showMessageDialog(null, contact + " added");
			} else {
				JOptionPane.showMessageDialog(null, contact + " doesn't exists");
			}
		}
		messageUI.revalidate();
		messageUI.repaint();
	}

	private class ServerResponse implements ServerListener {
		public void receive(Message message) {
			messageUI.addResponse(message);
		}

		public void receive(UserList userList) {
			ClientController.this.allUsers = userList;
			readContacts();
			for (int i = 0; i < allUsers.numberOfUsers(); i++) {
					if (contacts.exist(allUsers.getUser(i).getName())){
						allUsers.removeUser(allUsers.getUser(i));
					}
			}
			if (user.isConnected()) {
				messageUI.revalidate();
				messageUI.repaint();
			} else {
				user.setConnected(true);
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
