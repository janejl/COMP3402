import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class TicketServer extends UnicastRemoteObject implements TicketMachine {
	
	public static void main(String [] args) throws RemoteException, MalformedURLException {
		System.setSecurityManager(new SecurityManager());
		TicketMachine app = new TicketServer();
		Naming.rebind("TicketMachine", app);
		System.out.println("Service registered");
	}
	
	private int counter;
	public TicketServer() throws RemoteException {
		counter = 1000;
	}
	public int getTicket() throws RemoteException {
		return counter++;
	}
}
