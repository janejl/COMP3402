import java.rmi.Remote;
import java.rmi.RemoteException;

// Remote interface
// list the services that can be called by clients
public interface GameServer extends Remote{
	 int loginService(String user_name, String password) throws RemoteException;
	 boolean logoutService(String user_name) throws RemoteException;
	 int registerService(String user_name, String password) throws RemoteException;
}
