package ChatServer;

import java.net.Socket;

import resauces.*;

public interface ClientListener {
public void receive(Message message);
public void receive(User user);
public void disconnectedUser(User user);
}
