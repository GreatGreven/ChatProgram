package ChatClient;

import resources.*;

/**
 * Interface that allows a client to listen to the server
 * @author Oskar Engström Magnusson
 * 
 */
public interface ServerListener {
	
/**
 * Method for receiving a message from the server
 * @param message a message object
 */
public void receive(Message message);

/**
 * Method for receiving a UserList from the server 
 * @param userList a list of users
 */
public void receive(UserList userList);

/**
 * Method for when access to the server is denied
 */
public void accessDenied();
}
