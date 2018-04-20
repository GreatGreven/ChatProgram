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
	private ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
	private ArrayList<JButton> receivers = new ArrayList<JButton>();
	private ArrayList<JCheckBox> checkBoxesAll = new ArrayList<JCheckBox>();
	private ArrayList<JButton> receiversAll = new ArrayList<JButton>();
	private JPanel pnlContacts;
	private JPanel pnlOnline;
	private JList<String> listContacts;
	private JList<String> listOnline;
	private JPanel pnlContainer;

	
	public MessageUI(ClientController cont) {
		this.controller = cont;
		Dimension windowSize = new Dimension(500, 500);
		this.setPreferredSize(windowSize);
		this.setLayout(new BorderLayout());
		pnlWrite = writePanel();
		pnlRead = readPanel();
		pnlUsers = contactPanel();
		pnlUsers.setPreferredSize(new Dimension(150,0));
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

	private JPanel contactPanel(){
		JPanel panel = new JPanel();
		btnAddContact = new JButton("+ Add Contact");
		btnAddContact.addActionListener(new Listener());
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(btnAddContact, BorderLayout.NORTH);
		
		pnlContainer = new JPanel();
		JScrollPane scrPnUsers = new JScrollPane(pnlContainer);
		panel.add(scrPnUsers);
		pnlContainer.setLayout(new GridLayout(0, 1, 0, 0));
		
		pnlContacts = new JPanel();
		pnlContainer.add(pnlContacts);
		pnlContacts.setLayout(new BorderLayout(0, 0));
		JLabel lblContacts = new JLabel("Contacts");
		lblContacts.setHorizontalAlignment(SwingConstants.CENTER);
		pnlContacts.add(lblContacts, BorderLayout.NORTH);		
		listContacts = new JList<String>(populateContactList());
		pnlContacts.add(listContacts, BorderLayout.CENTER);
		
		pnlOnline = new JPanel();
		pnlContainer.add(pnlOnline);
		pnlOnline.setLayout(new BorderLayout(0, 0));
		JLabel lblOnline = new JLabel("Online");
		pnlOnline.add(lblOnline, BorderLayout.NORTH);
		lblOnline.setHorizontalAlignment(SwingConstants.CENTER);
		listOnline = new JList<String>(populateOnlineList());
		pnlOnline.add(listOnline, BorderLayout.CENTER);


		return panel;
	}

	private JPanel writeImagePanel() {
		lblImageFile = new JLabel("");
		btnImage = new JButton("Choose image");
		fc = new JFileChooser();
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
	}

	private String[] populateContactList() {
		UserList list = controller.getContacts();
		String[] dataList = new String[list.numberOfUsers()];
		for (int i = 0; i < list.numberOfUsers(); i++) {
			dataList[i] = list.getUser(i).getName();
		}
		return dataList;
	}

	private String[] populateOnlineList() {
		UserList list = controller.getAllUsers();
		String[] dataList = new String[list.numberOfUsers()];
		for (int i = 0; i < list.numberOfUsers(); i++) {
			dataList[i] = list.getUser(i).getName();
		}
		return dataList;
	}

	protected String getText() {
		return taWrite.getText();
	}

	protected String getImagePath() {
		return lblImageFile.getText();
	}

	protected ArrayList<String> getReceivers() {
		ArrayList<String> receiver = new ArrayList<String>();
		for (int i = 0; i < checkBoxes.size(); i++) {
			if (checkBoxes.get(i).isSelected()) {
				receiver.add(receivers.get(i).getToolTipText());
			}
		}
		return receiver;
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

	private class Listener implements ActionListener, KeyListener {

		@Override
		public void keyPressed(KeyEvent k) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent k) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent k) {
			// TODO Auto-generated method stub

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
			}
			if (a.getSource() == btnAddContact) {
				controller.addContact(JOptionPane.showInputDialog("Search for User"));
				scrollContactPane.revalidate();
				scrollContactPane.repaint();

			}
			for (int i = 0; i < receivers.size(); i++) {
				if (a.getSource() == receivers.get(i)) {
					checkBoxes.get(i).setSelected(true);
				}
			}
		}

	}

	public static void main(String[] args) {
		MessageUI ui = new MessageUI(null);
		JFrame frame = new JFrame("Messenger");
		frame.getContentPane().add(ui);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}
}
