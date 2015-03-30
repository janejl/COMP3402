import java.net.*;
import java.io.*;

public class EchoServer {

	public static void main(String[] args) {
		new EchoServer().go();
	}
	
	public void go() {
		try {
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(10000);
			System.out.println("Listening at 10000");
			while(true) {
				Socket s = ss.accept();
				System.out.println("Connection accepted");
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter out = new PrintWriter(s.getOutputStream());
				System.out.println("I/O streams prepared");
	
				try {
					while(true) {
						String message = in.readLine();
						System.out.println("Message '"+ message + "' received");
						out.println(message);
						out.flush();
					}
				} catch (IOException e) {
					System.out.println("Connection closed");
				}
			}
		} catch (IOException e) {
		}
	}
}