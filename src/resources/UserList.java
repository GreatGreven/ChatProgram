package resources;

import java.io.Serializable;
import java.util.ArrayList;



public class UserList implements Serializable { 
	private static final long serialVersionUID = -1968868156826365885L;
	private ArrayList<User> users;
	
	public UserList()  {
		users = new ArrayList<User>();
	}
	public  synchronized int numberOfUsers() {
		return users.size();
	}
	public synchronized void addUser(User user) {
		users.add(user);
	}

	public synchronized void removeUser(User user) {
		int index = users.indexOf(user);
		users.remove(index+1);
	}
	public synchronized UserList getUserList() {
		return this;
	}
	public synchronized User getUser(int i) {
		return users.get(i);
	}
	
	public synchronized boolean exist(String username){
		for (User u:users){
			if (username.equals(u.getName())){
				return true;
			}
		}
		return false;
	}
	
	public synchronized int indexOf(String username){
		for (int i = 0; i < users.size();i++){
			if (username.equals(users.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	public synchronized String toString(){
		String str = "";
		for (int i = 0; i < users.size();i++){
			str += users.get(i);
			if (i < users.size()-1){
				str += ", ";
			}
		}
		return str;
		
	}
}
