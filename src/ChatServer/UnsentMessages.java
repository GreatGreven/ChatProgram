package ChatServer;

import java.util.ArrayList;
import java.util.HashMap;

import resources.Message;
/**
 * Class that saves messages that hasn't been sent in an HashMap. The HashMap contains an key which is the user name
 * and an ArrayList which holds the message
 * @author Matthias Falk
 *
 */
public class UnsentMessages {
	private HashMap<String, ArrayList<Message>> unsent;
	
	/**
	 * Declares the unsent variable
	 */
	public UnsentMessages() {
		unsent = new HashMap<String, ArrayList<Message>>();
	}
	/**
	 * Synchronized method that puts an new ArrayList in the HashMap with the users name as an key
	 * @param user The Users name
	 * @param newList the list that will added
	 */
	public synchronized void put(String user, ArrayList<Message> newList){
		unsent.put(user, newList);
	}
	/**
	 * Synchronized method that returns the ArrayList assigned to the Users name
	 * @param user The Users name
	 * @return an ArrayList which holds the Messages 
	 */
	public synchronized ArrayList<Message> get(String user){
		return unsent.get(user);
	}
	/**
	 * Synchronized method that removes all Messages assigned to the Users name
	 * @param user The name of the User
	 */
	public synchronized void remove (String user) {
		unsent.remove(user);
	}
	/**
	 * Synchronized method that returns true if the HashMap contains the given key
	 * @param key The key that will be checked
	 * @return boolean 
	 */
	public synchronized boolean contains(String key){
		return unsent.containsKey(key);
	}
	
	
	
	

}
