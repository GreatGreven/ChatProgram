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
	protected JPanel pnlOnline;
	protected JList<String> listContacts;
	protected JList<String> listOnline;
	private JPanel pnlContainer;
	private ArrayList<String> receivers = new ArrayList<String>();
	private JPanel pnlProfile;
	private JLabel lblIcon;
//Model till JList f�r att kunna adda elements i efterhand.
	private DefaultListModel<String> modelOnlineList = new DefaultListModel<>();
	private DefaultListModel<String> modelContactList = new DefaultListModel<>();

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

	}

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

	private JPanel readPanel() {
		taRead = new JTextArea();
		scrollReadPane = new JScrollPane(taRead);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollReadPane);
		return panel;
	}

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
		pnlContacts.add(listContacts, BorderLayout.CENTER);

		pnlOnline = new JPanel();
		pnlContainer.add(pnlOnline);
		pnlOnline.setLayout(new BorderLayout(0, 0));
		JLabel lblOnline = new JLabel("Online");
		pnlOnline.add(lblOnline, BorderLayout.NORTH);
		lblOnline.setHorizontalAlignment(SwingConstants.CENTER);
		//2 NYA RADER
		listOnline = new JList<>(modelOnlineList);
		populateOnlineList();
		pnlOnline.add(listOnline, BorderLayout.CENTER);

		pnlProfile = new JPanel();
		panel.add(pnlProfile, BorderLayout.NORTH);
		pnlProfile.setLayout(new BorderLayout(0, 0));
		btnAddContact = new JButton("+ Add Contact");
		pnlProfile.add(btnAddContact, BorderLayout.SOUTH);

		lblIcon = new JLabel();
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setIcon(controller.getThisUser().getPicture());
		lblIcon.setMaximumSize(new Dimension(100, 100));
		pnlProfile.add(lblIcon, BorderLayout.CENTER);

		return panel;
	}

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

	private void initializeListeners() {
		Listener l = new Listener();
		btnSend.addActionListener(l);
		btnImage.addActionListener(l);
		listOnline.addMouseListener(l);
		listContacts.addMouseListener(l);
		btnAddContact.addActionListener(l);

	}

	private void populateContactList() {
		UserList list = controller.getContacts();
		for (int i = 0; i < list.numberOfUsers(); i++) {
			if(!modelContactList.contains(list.getUser(i).getName())){
				modelContactList.addElement(list.getUser(i).getName());
			}
		}
	}

	public void populateOnlineList() {
		UserList list = controller.getAllUsers();
		for (int i = 0; i < list.numberOfUsers(); i++) {
			if (!modelOnlineList.contains(list.getUser(i).getName())) {
				modelOnlineList.addElement(list.getUser(i).getName());
			}
		}
	}

	protected String getText() {
		return taWrite.getText();
	}

	protected String getImagePath() {
		return lblImageFile.getText();
	}

	protected ArrayList<String> getReceivers() {
		return receivers;
	}

	public void addResponse(Message message) {
		String content = taRead.getText();
		String append = message.getText();
		this.taRead.setText(content + "\n" + append);
		ImageIcon image = message.getImage();
		if (image != null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JFrame iconFrame = new JFrame("Image");
					iconFrame.getContentPane().add(new JLabel(image));
				}
			});
		}
	}

	private class Listener implements ActionListener, KeyListener, MouseListener {

		@Override
		public void keyPressed(KeyEvent k) {
		}

		@Override
		public void keyReleased(KeyEvent k) {
		}

		@Override
		public void keyTyped(KeyEvent k) {
		}

		@Override
		public void actionPerformed(ActionEvent a) {
			if (a.getSource() == btnImage) {
				int fileValue = fc.showSaveDialog(null);
				if (fileValue == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					lblImageFile.setText(file.getPath());
				}
			}
			if (a.getSource() == btnSend) {
				controller.send();
				receivers.clear();
			}
			if (a.getSource() == btnAddContact) {
				controller.addContact(JOptionPane.showInputDialog("Search for User"));
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == listContacts) {
				if (!receivers.contains(listContacts.getSelectedValue())){
					receivers.add(listContacts.getSelectedValue());
				}
			} else if (e.getSource() == listOnline) {
				if (!receivers.contains(listOnline.getSelectedValue())){
					receivers.add(listOnline.getSelectedValue());					
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}
}
