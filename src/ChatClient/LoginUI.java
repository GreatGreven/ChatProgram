package ChatClient;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageFilter;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LoginUI extends JPanel{
	private ClientController controller;
	private JPanel pnlContent = new JPanel(new GridLayout(2,1,0,0));
	private JPanel pnlButtons = new JPanel(new GridLayout(1,2,5,0));
	private JButton btnImage = new JButton("Choose Icon");
	private JButton btnLogin = new JButton("Log In");
	private JTextField tfName = new JTextField();
	private JPanel pnlTextField;
	private JPanel pnlBtnImage;
	private JPanel pnlBtnLogin;
	private JFileChooser fc;
	private File file;
	private JLabel lblFile = new JLabel();
	
	
	public LoginUI(ClientController cont) {
		this.controller = cont;
		pnlBtnLogin = new JPanel();
		pnlBtnImage = new JPanel();
		pnlTextField = new JPanel();
		int width = 250;
		int height = 100;
		Dimension windowSize = new Dimension(width,height);
		this.setPreferredSize(windowSize);
		this.setLayout(new BorderLayout());

		fc = new JFileChooser(new File(System.getProperty("user.home"), "Pictures"));
		fc.setDialogTitle("Image chooser");
		FileFilter imageFilter = new FileNameExtensionFilter(
			    "Image files", ImageIO.getReaderFileSuffixes());
		fc.addChoosableFileFilter(imageFilter);
		fc.setAcceptAllFileFilterUsed(false);
		
		pnlContent.add(pnlTextField);
		tfName.setToolTipText("Name");
		tfName.setColumns(20);
		pnlTextField.add(tfName);

		pnlButtons.add(pnlBtnImage);
		btnImage.setToolTipText("Image");
		pnlBtnImage.add(btnImage);
		
		pnlButtons.add(pnlBtnLogin);
		btnLogin.setToolTipText("Login");
		pnlBtnLogin.add(btnLogin);
		
		pnlContent.add(pnlButtons);
		this.add(pnlContent, BorderLayout.CENTER);
		lblFile.setPreferredSize(new Dimension(0, 20));
		this.add(lblFile, BorderLayout.SOUTH);
		
		initializeListeners();
		
	}
	
	private void initializeListeners(){
		btnImage.addActionListener(new AL());
		btnLogin.addActionListener(new AL());
	}
	
	public String getName(){
		String name = tfName.getText();
		return name;
	}
	
	public String getIconPath(){
		String filename = lblFile.getText();
		return filename;
	}
	
	private class AL implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnImage){
				int fileValue = fc.showSaveDialog(null);
				if (fileValue == JFileChooser.APPROVE_OPTION){
					file = fc.getSelectedFile();
					lblFile.setText(file.getAbsolutePath());
				}
			}
			if (e.getSource() == btnLogin){
				controller.login();
			}
		}
	}
	
	public static void main(String[]args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				JFrame frame = new JFrame("Login");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.getContentPane().add(new LoginUI(new ClientController("localhost", 3300)));
				frame.pack();
			}
		});
	}

}
