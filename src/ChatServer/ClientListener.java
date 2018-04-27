package ChatServer;

import resources.*;
/**
 * Interface that allows the server to listen to a client
 * @author Oskar Engström Magnusson
 *
 */
public interface ClientListener {
	
/**
 * Method for receiving a message from a client
 * @param message a message object
 */
public void receive(Message message);

/**
 * Method for receiving a user from a client
 * @param user a User object
 */
public void receive(User user);

/**
 * Method for when a user disconnects from the server 
 * @param user a User object
 */
public void disconnectedUser(User user);
}
