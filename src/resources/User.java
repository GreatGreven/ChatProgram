package resources;

import java.io.Serializable;

import javax.swing.*;

public class User implements Serializable{
	private static final long serialVersionUID = -773659708777609854L;
	private String name;
	private ImageIcon picture;
	private boolean connected;

	public User(String name){
		this.name = name;
		this.picture = new ImageIcon("images/no_icon.png");
	}
	
	public User(String name, ImageIcon pic){
		this.name = name;
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
