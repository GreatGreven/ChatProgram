package resources;

import java.io.Serializable;

import javax.swing.*;
/**
 * A Class represents a User 
 * @author elinolsson
 *
 */
public class User implements Serializable{
	private String name;
	private ImageIcon picture;
	private boolean connected;

	/**
	 * Constructor
	 * @param name - the users name
	 * @param pic - the users profile picture 
	 * if the user dont choose a pic, a standardicon picture will be given. 
	 */
	public User(String name, ImageIcon pic){
		this.name = name;
		if (pic == null){
			picture = new ImageIcon("images/no_icon.png");
		}
		this.picture = pic;
	}
/**
 * Getter for the users name
 * @return - the users name 
 */
	public String getName(){
		return name;
	}
	/**
	 * Getter for the user picture
	 * @return - the users picture 
	 */
	public ImageIcon getPicture(){
		return picture;
	}
	/**
	 * Method that return the hashCode 
	 * @return - hashcode
	 */
	public int hashCode(){
		return name.hashCode();
	}
	/**
	 * Method that returns true or false if users name equals the incomming Object anObject
	 * @return true or false 
	 */
	public boolean equals(Object anObject){
		return name.equals(anObject);
	}
	/**
	 * Setter for the users profile picture 
	 * @param pic - the users picture 
	 */
	public void setPicture(ImageIcon pic){
		this.picture = pic;
	}
	/**
	 * Boolean, returns true or false if the user is connected
	 * @return connected - either true or false 
	 */
	public boolean isConnected() {
		return connected;
	}
/**
 * Setter for the current value of connected, either true or false
 * @param connected - the result of isConnected 
 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	/**
	 * Method that returns the users name
	 * @return name - the users name
	 */
	public String toString(){
		return name;
	}
}
