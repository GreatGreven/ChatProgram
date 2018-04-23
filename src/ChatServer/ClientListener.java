package ChatServer;

import resources.*;

public interface ClientListener {
public void receive(Message message);
public void receive(User user);
public void disconnectedUser(User user);
}
