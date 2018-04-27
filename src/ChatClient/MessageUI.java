package ChatClient;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import resources.*;

/**
 * 
 * @author Eric Grevillius
 *
 *         Class that inherits from JPanel and is used as a GUI for sending and
 *         receiving messages in the program.
 */
public class MessageUI extends JPanel {
	private static final long serialVersionUID = 6091921199167131315L;
	private ClientController controller;
	private JPanel pnlRead;
	private JPanel pnlWrite;
	private JPanel pnlUsers;
	private JTextArea taRead;
	private JTextArea taWrite;
	private JButton btnSend;
	private JButton btnImage;
	private JButton btnAddContact;
	private JLabel lblImageFile;
	private JScrollPane scrollReadPane;
	private JScrollPane scrollContactPane;
	private JFileChooser fc;
	private File file;
	private JPanel pnlContacts;
	private JPanel pnlOnline;
	private JList<String> listContacts;
	private JList<String> listOnline;
	private JPanel pnlContainer;
	private ArrayList<String> receivers = new ArrayList<String>();
	private JPanel pnlProfile;
	private JLabel lblIcon;
	private DefaultListModel<String> modelOnlineList = new DefaultListModel<>();
	private DefaultListModel<String> modelContactList = new DefaultListModel<>();

	/**
	 * Constructor creates a GUI for sending and receiving messages.
	 * 
	 * @param cont
	 *            A link to the controller.
	 */
	public MessageUI(ClientController cont) {
		this.controller = cont;
		Dimension windowSize = new Dimension(500, 500);
		this.setPreferredSize(windowSize);
		this.setLayout(new BorderLayout());
		pnlWrite = writePanel();
		pnlRead = readPanel();
		pnlUsers = contactPanel();
		pnlUsers.setPreferredSize(new Dimension(150, 0));
		this.add(pnlRead, BorderLayout.CENTER);
		this.add(pnlWrite, BorderLayout.SOUTH);
		this.add(pnlUsers, BorderLayout.EAST);

		Boolean editable = true;
		taRead.setEditable(!editable);
		taWrite.setEditable(editable);
		initializeListeners();
		controller.readContacts();
		populateContactList();
	}

	/**
	 * Creates the panel used for writing a message.
	 * 
	 * @return the write-panel.
	 */
	private JPanel writePanel() {
		taWrite = new JTextArea();
		btnSend = new JButton("Send");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(10, 100));
		panel.add(writeImagePanel(), BorderLayout.NORTH);
		panel.add(taWrite, BorderLayout.CENTER);
		panel.add(btnSend, BorderLayout.EAST);

		return panel;
	}

	/**
	 * Creates the panel used for reading messages.
	 * 
	 * @return the read-panel.
	 */
	private JPanel readPanel() {
		taRead = new JTextArea();
		scrollReadPane = new JScrollPane(taRead);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollReadPane);
		return panel;
	}

	/**
	 * Creates the panel for browsing saved contacts and online users.
	 * 
	 * @return the contact-panel.
	 */
	private JPanel contactPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));

		pnlContainer = new JPanel();
		scrollContactPane = new JScrollPane(pnlContainer);
		panel.add(scrollContactPane);
		pnlContainer.setLayout(new GridLayout(0, 1, 0, 0));

		pnlContacts = new JPanel();
		pnlContainer.add(pnlContacts);
		pnlContacts.setLayout(new BorderLayout(0, 0));
		JLabel lblContacts = new JLabel("Contacts");
		lblContacts.setHorizontalAlignment(SwingConstants.CENTER);
		pnlContacts.add(lblContacts, BorderLayout.NORTH);
		listContacts = new JList<String>(modelContactList);
		populateContactList();
		listContacts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pnlContacts.add(listContacts, BorderLayout.CENTER);

		pnlOnline = new JPanel();
		pnlContainer.add(pnlOnline);
		pnlOnline.setLayout(new BorderLayout(0, 0));
		JLabel lblOnline = new JLabel("Online");
		pnlOnline.add(lblOnline, BorderLayout.NORTH);
		lblOnline.setHorizontalAlignment(SwingConstants.CENTER);
		// 2 NYA RADER
		listOnline = new JList<>(modelOnlineList);
		populateOnlineList();
		listOnline.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		pnlOnline.add(listOnline, BorderLayout.CENTER);

		pnlProfile = new JPanel();
		panel.add(pnlProfile, BorderLayout.NORTH);
		pnlProfile.setLayout(new BorderLayout(0, 0));
		btnAddContact = new JButton("+ Add Contact");
		pnlProfile.add(btnAddContact, BorderLayout.SOUTH);

		lblIcon = new JLabel(new ImageIcon(
				controller.getThisUser().getPicture().getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setToolTipText(controller.getThisUser().getName());
		lblIcon.setMaximumSize(new Dimension(100, 100));
		pnlProfile.add(lblIcon, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Creates a panel within the write-panel that is for selecting an image
	 * from a file to send.
	 * 
	 * @return the Image-selecting panel.
	 */
	private JPanel writeImagePanel() {
		lblImageFile = new JLabel("");
		btnImage = new JButton("Choose image");
		fc = new JFileChooser(new File(System.getProperty("user.home"), "Pictures"));
		fc.setDialogTitle("Image chooser");
		FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
		fc.addChoosableFileFilter(imageFilter);
		fc.setAcceptAllFileFilterUsed(false);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(btnImage, BorderLayout.WEST);
		panel.add(lblImageFile, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Initializes a single multi-listener to all elements that needs a
	 * listener.
	 */
	private void initializeListeners() {
		AL l = new AL();
		btnSend.addActionListener(l);
		btnImage.addActionListener(l);
		btnAddContact.addActionListener(l);
	}

	/**
	 * Populates the list showing contacts.
	 */
	protected void populateContactList() {
		UserList list = controller.getContacts();
		for (int i = 0; i < list.numberOfUsers(); i++) {
			if (!modelContactList.contains(list.getUser(i).getName())) {
				modelContactList.addElement(list.getUser(i).getName());
			}
		}
	}

	/**
	 * Populates the list showing online users.
	 */
	protected void populateOnlineList() {
		UserList list = controller.getAllUsers();
		for (int i = 0; i < list.numberOfUsers(); i++) {
			if (!modelOnlineList.contains(list.getUser(i).getName())) {
				modelOnlineList.addElement(list.getUser(i).getName());
			}
		}
	}

	/**
	 * Returns the text written in the text area for writing messages.
	 * 
	 * @return The text of the written message.
	 */
	protected String getText() {
		return taWrite.getText();
	}

	/**
	 * Returns the file-path for the chosen image.
	 * 
	 * @return
	 */
	protected String getImagePath() {
		return lblImageFile.getText();
	}

	/**
	 * Returns an ArrayList of strings with all the users that has been added as
	 * receivers.
	 * 
	 * @return The ArrayList of receivers.
	 */
	protected ArrayList<String> getReceivers() {
		return receivers;
	}

	/**
	 * Adds a message to the display area of the GUI and a pop-up window
	 * displaying an image from the message if there is one.
	 * 
	 * @param message
	 *            Incoming message.
	 */
	public void addResponse(Message message) {
		String content = taRead.getText();
		String append = "From " + message.getSender().getName() + " : " + message.getText();
		this.taRead.setText(content + append + "\n");
		ImageIcon image = message.getImage();
		if (image != null) {
			JOptionPane.showMessageDialog(null, null, "Message from " + message.getSender().getName(),
					JOptionPane.PLAIN_MESSAGE, image);
		}
	}

	/**
	 * Inner class that implements an actionlistener.
	 * 
	 * @author Eric Grevillius
	 *
	 */
	private class AL implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			if (a.getSource() == btnImage) {
				int fileValue = fc.showSaveDialog(null);
				if (fileValue == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					lblImageFile.setText(file.getPath());
				}
			}
			if (a.getSource() == btnSend) {
				java.util.List<String> listC = listContacts.getSelectedValuesList();
				java.util.List<String> listO = listOnline.getSelectedValuesList();
				for (int i = 0; i < listO.size() || i < listC.size(); i++) {
					if (i < listO.size()) {
						if (!receivers.contains(listO.get(i))) {
							receivers.add(listO.get(i));
						}
					}
					if (i < listC.size()) {
						if (!receivers.contains(listC.get(i))) {
							receivers.add(listC.get(i));
						}
					}
				}
				controller.send();
				receivers.clear();
				lblImageFile.setText("");
				taWrite.setText("");
			}
			if (a.getSource() == btnAddContact) {
				controller.addContact(JOptionPane.showInputDialog("Search for User"));
			}
		}
	}
}
