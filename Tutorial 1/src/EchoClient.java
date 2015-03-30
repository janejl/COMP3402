import java.net.*;
import java.util.Scanner;
import java.io.*;

public class EchoClient {

	public static void main(String[] args) {
		new EchoClient().go();
	}
	public void go() {
		try {
			Socket s = new Socket("localhost", 10000);
			System.out.println("Connection established");
			PrintWriter out = new PrintWriter(s.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			System.out.println("I/O streams prepared");
			
			Scanner scanner = new Scanner(System.in);
			
			boolean loop = true;
			while(loop) {
				String line = scanner.nextLine();
				if(line == null || line.isEmpty()) {
					loop = false;
				} else {
					out.println(line);
					out.flush();
					String message = in.readLine();
					System.out.println(message);
				}
			}
			scanner.close();
			s.close();
		} catch(IOException e){
		}
		
	}
}