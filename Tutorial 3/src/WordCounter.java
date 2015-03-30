import java.rmi.*;
import java.rmi.server.*;

@SuppressWarnings("serial")
public class WordCounter extends UnicastRemoteObject implements WordCount{

	public static void main(String[] args) {
		try {
			WordCounter app = new WordCounter();			
			System.setSecurityManager(new SecurityManager());// Security Manager
			
			Naming.rebind("WordCounter", app);//it already regards we are using local
			System.out.println("Service registered");
			
			//int count = app.count("The quick brown fox jumps over a lazy dog");
			//System.out.println("There are "+count+" words");
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
		}
	}
	
	public WordCounter() throws RemoteException{}
	
	public int count (String message) throws RemoteException{
		return message.split(" +").length;
	}

}