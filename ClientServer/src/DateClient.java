import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class DateClient {

	/**
	 * Runs the client as an application. First it displays a dialog box asking
	 * for the IP address or hostname of a host running the date server, then
	 * connects to it and displays the date that it serves.
	 * 
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {
		Socket s = new Socket("localhost", 9090);
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		out.println("$R<300123412341234123412341234");
		BufferedReader input = new BufferedReader(new InputStreamReader(
				s.getInputStream()));
		System.out.println("Client: " + input.readLine());
	}
}
