package resouces;

import java.util.LinkedList;

public class MessageList {
	private LinkedList<Message> messages; 
	
	public MessageList(){
		messages = new LinkedList<Message>();
	}
	
	public  synchronized int numberOfMessages() {
		return messages.size();
	}
	public synchronized  void addMessage(Message message) {
		messages.add(message);
	}

	public synchronized  void removeMessage(Message message) {
		messages.remove(message);
	}
	public synchronized MessageList getMessageList() {
		return this;
	}
	public synchronized Message getMessage(int i) {
		return messages.get(i);
	}}
