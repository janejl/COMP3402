import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.*;
import java.rmi.server.*;

@SuppressWarnings("serial")
public class WordCounter extends UnicastRemoteObject implements WordCount {

	public static void main(String[] args) {
		try {
			WordCounter app = new WordCounter();
			System.setSecurityManager(new SecurityManager());
			Naming.rebind("WordCounter", app);
			System.out.println("Service registered");			
			// int count = app.count("The quick brown fox jumps over a lazy dog");
			// System.out.println("There are "+count+" words");
			
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
		}		
	}
	
	public WordCounter() throws RemoteException {}
	public int count(String message) throws RemoteException {
		try {
			PrintWriter out = new PrintWriter(new FileWriter("message.log"));
			out.println(message);
			out.flush();
			out.close();
		} catch(IOException e) {}
		return message.split(" +").length;
	}

}
