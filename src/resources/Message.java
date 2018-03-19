package resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;

public class Message implements Serializable{
	private User sender;
	private UserList receivers;
	private String text;
	private ImageIcon image;
	private Date date = new Date();
	private Date delivered;
	private Date received;

	public Message(User sender, UserList receivers){
		this.sender = sender;
		this.receivers = receivers;
	}
	
	public Message(User sender, UserList receivers , String text) {
		this(sender, receivers);
		this.text = text;
	}

	public Message(User sender, UserList receivers, String text, ImageIcon image) {
		this (sender,receivers,text);
		this.image = image;
	}

	public User getSender() {
		return sender;
	}

	public UserList getReceivers() {
		return receivers;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setDeliveredDate(){
		if (delivered == null) {
			this.delivered = new Date();
		}
	}
	
	public Date getDeliveredDate() {
		return delivered;
	}

	public void setReceivedDate(){
		if (received == null) {
			this.received = new Date();
		}
	}
	
	public Date getReceivedDate() {
		return received;
	}
	
	public String toString(){
		return "(" + super.toString() + ") Sender: " + sender + " Receivers: " + receivers + " Text: " + text + 
				" Image: " + image + " Delivered: " + delivered + " Received: " + received;
	}
}
