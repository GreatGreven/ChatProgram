package ChatServer;

import java.util.ArrayList;
import java.util.HashMap;

import resources.Message;

public class UnsentMessages {
	private HashMap<String, ArrayList<Message>> unsent;

	public UnsentMessages() {
		unsent = new HashMap<String, ArrayList<Message>>();
	}
	
	public synchronized void put(String user, ArrayList<Message> newList){
		unsent.put(user, newList);
	}
	
	public synchronized ArrayList<Message> get(String user){
		return unsent.get(user);
	}
	public synchronized void remove (String user) {
		unsent.remove(user);
	}
	public synchronized boolean contains(String key){
		return unsent.containsKey(key);
	}
	
	
	
	

}
