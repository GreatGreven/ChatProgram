package ChatClient;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import resouces.*;

public class MessageUI extends JPanel {
	private ClientController controller;
	private JPanel pnlRead;
	private JPanel pnlWrite;
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
	
	public MessageUI(ClientController cont) {
		this.controller = cont;
		Dimension windowSize = new Dimension(500,500);
		this.setPreferredSize(windowSize);
		this.setLayout(new BorderLayout());
		pnlWrite = writePanel();
		pnlRead = readPanel();
		scrollContactPane = new JScrollPane(contactPanel());
		this.add(pnlRead, BorderLayout.CENTER);
		this.add(pnlWrite, BorderLayout.SOUTH);
		this.add(scrollContactPane, BorderLayout.EAST);
		
		Boolean editable = true;
		taRead.setEditable(!editable);
		taWrite.setEditable(editable);
		initializeListeners();
		
	}
	private JPanel writePanel(){
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
	
	private JPanel readPanel(){
		taRead = new JTextArea();
		scrollReadPane = new JScrollPane(taRead);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollReadPane);
		return panel;
	}
	
	private JPanel contactPanel(){
		populateContactList();
		JPanel panel = new JPanel(new BorderLayout());
		btnAddContact = new JButton("+ Add Contact");
		btnAddContact.addActionListener(new Listener());
		JPanel pnlUsers = new JPanel(new GridLayout(100,2));
		for(int i=0;i<checkBoxes.size();i++){
			pnlUsers.add(checkBoxes.get(i));
			pnlUsers.add(receivers.get(i));
		}
		panel.add(pnlUsers,BorderLayout.CENTER);
		panel.add(btnAddContact,BorderLayout.NORTH);
		return panel;
	}
		
	private JPanel writeImagePanel(){
		lblImageFile = new JLabel("");
		btnImage = new JButton("Choose image");
		fc = new JFileChooser();
		fc.setDialogTitle("Image chooser");
		FileFilter imageFilter = new FileNameExtensionFilter(
			    "Image files", ImageIO.getReaderFileSuffixes());
		fc.addChoosableFileFilter(imageFilter);
		fc.setAcceptAllFileFilterUsed(false);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(btnImage, BorderLayout.WEST);
		panel.add(lblImageFile, BorderLayout.CENTER);
		return panel;
	}
	
	private void initializeListeners(){
		Listener l = new Listener();
		btnSend.addActionListener(l);
		btnImage.addActionListener(l);
	}
	
	private void populateContactList(){
		UserList list = controller.getContacts();
		for (int i = 0 ; i < list.numberOfUsers();i++){
			checkBoxes.add(new JCheckBox());
			receivers.add(new JButton());
			receivers.get(i).setIcon(list.getUser(i).getPicture());
			receivers.get(i).setToolTipText(list.getUser(i).getName());
			receivers.get(i).addActionListener(new Listener());
		}
	}
	
	
	
	protected String getText(){
		return taWrite.getText();
	}
	
	protected String getImagePath(){
		return lblImageFile.getText();
	}
	
	protected ArrayList<String> getReceivers(){
		ArrayList<String> receiver = new ArrayList<String>();
		for (int i = 0; i < checkBoxes.size();i++){
			if (checkBoxes.get(i).isSelected()){
				receiver.add(receivers.get(i).getToolTipText());
			}
		}
		return receiver;
	}
	
	public void addResponse(Message message){
		String content = taRead.getText();
		String append = message.getText();
		this.taRead.setText(content + "\n" + append);
		ImageIcon image = message.getImage();
		if (image != null){
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
					JFrame iconFrame = new JFrame("Image");
					iconFrame.getContentPane().add(new JLabel(image));
				}
			});
		}
	}
	
	private class Listener implements ActionListener, KeyListener{

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
			if (a.getSource()==btnImage){
				int fileValue = fc.showSaveDialog(null);
				if (fileValue == JFileChooser.APPROVE_OPTION){
					file = fc.getSelectedFile();
					lblImageFile.setText(file.getPath());
				}
			}
			if (a.getSource()==btnSend){
				controller.send();
			}
			if (a.getSource()==btnAddContact){
				controller.addContact(JOptionPane.showInputDialog("Search for User"));
				scrollContactPane.revalidate();
				scrollContactPane.repaint();
				
			}
			for (int i = 0; i < receivers.size();i++){
				if (a.getSource()==receivers.get(i)){
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
