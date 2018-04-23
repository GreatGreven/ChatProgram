package resources;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;

public class Message implements Serializable{
	private static final long serialVersionUID = -8548295545248394817L;
	private User sender;
	private UserList receivers;
	private String text;
	private ImageIcon image;
	private String delivered;
	private String received;

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
			this.delivered = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
	}
	
	public String getDeliveredDate() {
		return delivered;
	}

	public void setReceivedDate(){
		if (received == null) {
			this.received = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
	}
	
	public String getReceivedDate() {
		return received;
	}
	
	public String toString(){
		return "Sender: " + sender + " Receivers: " + receivers + " Text: " + text + 
				" Image: " + image + " Delivered: " + delivered + " Received: " + received;
	}
}
