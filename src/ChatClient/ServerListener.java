package ChatClient;

import resources.*;

public interface ServerListener {
public void receive(Message message);
public void receive(UserList userList);
public void accessDenied();
}
