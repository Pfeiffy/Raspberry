import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

/**
 * A TCP server that runs on port 9090. When a client connects, it sends the
 * client the current date and time, then closes the connection with that
 * client. Arguably just about the simplest server you can write.
 */
public class DateServer {

	static int pitch = 0;
	static int yaw = 0;
	static int roll = 0;
	static int throttle = 0;
	static int aux1 = 0;
	static int aux2 = 0;
	static int pwmDirekt = 300; // direkte Eingabe der PWM-Werte
	static int pwmAlter = 310; // Änderumng der PWM-Werte
	static int setFlyMode = 320; // Flugmodus setzen, wie women, angel

	public static void main(String[] args) throws IOException {

		new Thread() {
			public void run() {
				try {
					ServerSocket listener = new ServerSocket(9090);
					try {
						System.out.println("Server gestartet");

						while (true) {
							Socket socket = listener.accept();
							try {
								while (true) {

									BufferedReader input = new BufferedReader(
											new InputStreamReader(socket.getInputStream()));
									String answer = input.readLine();
									System.out.println(answer + "(server)");
									String rueckAntwort = befehlsverarbeitung(answer);
									// Rückantwort senden
									PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
									out.println(rueckAntwort);

								}

							} catch (SocketException e) {
								// FA Wird geworfen, wenn die Verbindung vom
								// Client beendet
								// wurde.
								// FA Aus diesem Grund, machen wir hier nichts,
								// auser dass
								// wir den
								// FA Socket im finally Block aufrÃ¤umen.
							} finally {
								if (null != socket && false == socket.isClosed()) {
									socket.close();
								}
								socket = null;
							}
						}
					} finally {
						listener.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Thread Running");
			}
		}.start();

	}

	private static String befehlsverarbeitung(String answer) {
		// TODO Auto-generated method stub
		String returnCode = "";
		try {

			if (answer.substring(0, 3).equalsIgnoreCase("$R<")) {
				int code = Integer.parseInt(answer.substring(3, 6));
				if (code == pwmDirekt) {
					verabeitepwmDirekt(answer);
				}
				if (code == pwmAlter) {
					verabeitepwmAlter(answer);
				}
				if (code == setFlyMode) {
					verabeitesetFlyMode(answer);
				}
				anzeigeMotorDaten();
				returnCode = "$R>Satzformart verarbeitet";
			} else {
				returnCode = "$R>falsches Satzformart";
			}
		} catch (Exception e) {

			resetMotorsFail();
			returnCode = "$R>Fehler in Empfangsverarbeitung";
		}

		return returnCode;
	}

	private static void anzeigeMotorDaten() {
		System.out.println(" anzeigeMotorDaten: ");
		System.out.println(" pitch: " + pitch);
		System.out.println(" yaw: " + yaw);
		System.out.println(" roll: " + roll);
		System.out.println(" throttle: " + throttle);
		System.out.println(" aux1: " + aux1);
		System.out.println(" aux2: " + aux2);
	}

	private static void resetMotorsFail() {
		pitch = 1500;
		yaw = 1500;
		roll = 1500;
		throttle = 1200;

	}

	private static void verabeitesetFlyMode(String answer) {

	}

	private static void verabeitepwmAlter(String answer) {
		// TODO Auto-generated method stub

	}

	private static void verabeitepwmDirekt(String answer) {
		// $R<300123412341234123412341234
		// 012345678901234567890123456789
		pitch = Integer.parseInt(answer.substring(6, 10));
		yaw = Integer.parseInt(answer.substring(10, 14));
		roll = Integer.parseInt(answer.substring(14, 18));
		throttle = Integer.parseInt(answer.substring(18, 22));
		aux1 = Integer.parseInt(answer.substring(22, 26));
		aux2 = Integer.parseInt(answer.substring(26, 30));

	}

}
