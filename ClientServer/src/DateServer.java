import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class DateServer {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                	while (true) {
                	BufferedReader input =
            	            new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	        String answer = input.readLine();
            	        System.out.println(answer + "(server");
                        PrintWriter out =
                                new PrintWriter(socket.getOutputStream(), true);
                            out.println(answer+ "ist angekommen und verarbeitet");
                            
                            System.out.println(">");
                	}
                    
                } catch (SocketException e) {
                	// FA Wird geworfen, wenn die Verbindung vom Client beendet wurde.
                	// FA Aus diesem Grund, machen wir hier nichts, auser dass wir den 
                	// FA Socket im finally Block aufräumen.
                	e.printStackTrace();
                } finally {
                	if (null != socket && false == socket.isClosed()) {
                    	socket.close();
                	}
                	socket = null;
                }
            }
        }
        finally {
            listener.close();
        }
    }
}
