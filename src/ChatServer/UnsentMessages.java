package ChatServer;

import java.util.ArrayList;
import java.util.HashMap;

import resauces.Message;
import resauces.MessageList;
import resauces.User;

public class UnsentMessages {
	private HashMap<String, ArrayList<Message>> unsent = new HashMap<String, ArrayList<Message>>();

	public UnsentMessages() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void put(String user, ArrayList<Message> newList){
		unsent.put(user, newList);
	}
	
	public synchronized ArrayList<Message> get(String user){
		return unsent.get(user);
	}
	
	public synchronized boolean contains(String key){
		return unsent.containsKey(key);
	}
	
	
	
	

}
