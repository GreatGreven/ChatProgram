package resouces;

import java.io.Serializable;

import javax.swing.*;

public class User implements Serializable{
	private String name;
	private ImageIcon picture;
	private boolean connected;

	public User(String name, ImageIcon pic){
		this.name = name;
		if (pic == null){
			picture = new ImageIcon("images/no_icon.png");
		}
		this.picture = pic;
	}

	public String getName(){
		return name;
	}
	
	public ImageIcon getPicture(){
		return picture;
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Object anObject){
		return name.equals(anObject);
	}
	
	public void setPicture(ImageIcon pic){
		this.picture = pic;
	}
	
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public String toString(){
		return name;
	}
}
