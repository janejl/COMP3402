import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Customer {
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		TicketMachine app = (TicketMachine)Naming.lookup("TicketMachine");
		int ticket = app.getTicket();
		System.out.println("Got ticket number: "+ ticket);
	}

}
