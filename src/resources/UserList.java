package resources;

/**
 * A class that represents a userlist. All methods is synchonized which is a security matter 
 * @author elinolsson
 */
import java.io.Serializable;
import java.util.ArrayList;

public class UserList implements Serializable {
	private static final long serialVersionUID = -1968868156826365885L;
	private ArrayList<User> users;

	/**
	 * constructor creates a arraylist of users
	 */
	public UserList() {
		users = new ArrayList<User>();
	}

	/**
	 * returns the number of users in the list
	 * 
	 * @return - the size of the array
	 */
	public synchronized int numberOfUsers() {
		return users.size();
	}

	/**
	 * adds a user to the list
	 * 
	 * @param user
	 *            - the user to be added
	 */
	public synchronized void addUser(User user) {
		users.add(user);
	}

	/**
	 * removes a user form the list
	 * 
	 * @param user
	 *            - the user to be removed
	 */
	public synchronized void removeUser(User user) {
		int index = indexOf(user.getName());
		users.remove(index);
	}

	/**
	 * Getter for the userlist
	 * 
	 * @return - the list
	 */
	public synchronized UserList getUserList() {
		return this;
	}

	/**
	 * gets the user at the i position
	 * 
	 * @param i
	 *            - the position in the array
	 * @return - the user at pos i
	 */
	public synchronized User getUser(int i) {
		return users.get(i);
	}

	/**
	 * if the user exist in the list returns true, otherwise false
	 * 
	 * @param username
	 *            - the user name
	 * @return true or false
	 */
	public synchronized boolean exist(String username) {
		for (User u : users) {
			if (username.equals(u.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the index of the user. if the user doesn't exist in the list, -1
	 * will be returned
	 * 
	 * @param username
	 *            - the username
	 * @return either i or -1
	 */
	public synchronized int indexOf(String username) {
		for (int i = 0; i < users.size(); i++) {
			if (username.equals(users.get(i).getName())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Method that prints out the users in the list
	 * 
	 * @return the string
	 */
	public synchronized String toString() {
		String str = "";
		for (int i = 0; i < users.size(); i++) {
			str += users.get(i);
			if (i < users.size() - 1) {
				str += ", ";
			}
		}
		return str;

	}
}
