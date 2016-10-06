package readserialport;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;

import ocss.nmea.api.NMEAParser;

/**
 * Just reads the GPS data. No parsing, just raw data.
 */
public class GPSModule {

	private static double lat = 0;
	private static double lon = 0;
	private static double alt = 0;
	private static double richtung = 0;
	private static double speed = 0;
	private static String dataAkt = "";
	private static String dataVor = "";
	private static String data = " ";
	private static String Protokoll = "";
	private static String xString = "";

	public GPSModule() {

		new Thread() {
			public void run() {
				try {

					//
					int br = Integer.parseInt(System.getProperty("baud.rate", "9600"));
					String port = System.getProperty("/dev/ttyAMA0", "/dev/ttyAMA0");

					System.out.println("Serial Communication.");
					System.out.println(" ... connect using settings: " + Integer.toString(br) + ", N, 8, 1");
					System.out.println(" ... data received on serial port should be displayed below.");

					// create an instance of the serial communications class
					final Serial serial = SerialFactory.createInstance();

					// create and register the serial data listener
					serial.addListener(new SerialDataListener() {
						@Override
						public void dataReceived(SerialDataEvent event) {
							boolean stop = false;
							if (stop) {
								System.exit(1);
							}


							String data = event.getData().replaceAll("\n", "");
							xString = xString + data;
							// gibt es in diesem String 2 mal?, dann
							// verarbeitung
							try {
								if (countChar(xString, '$') > 1) {
									// Position des ersten $ ermitteln
									int pos = xString.indexOf("$");
									Protokoll = xString.substring(pos, xString.lastIndexOf("$"));
									// Protokoll verarbeiten
									String protokollname = xString.substring(pos, pos + 6);
									xString = "";
									if (protokollname.equals("$GPRMC")) {
										parseGPRMC(Protokoll);

									}
									if (protokollname.equals("$GPGGA")) {
										parseGPGGA(Protokoll);

									}

									// System.out.println("Protokoll: " +
									// protokoll);
									// xString zurücksetzen

								}

							} catch (Exception e) {
								System.out.println("fehlerhaftes Protokoll: " +Protokoll);
								System.out.println("xString: " +xString);
								e.printStackTrace();
								xString = "";
							}

							// System.out.println(xString);
//							System.out.println(
//									"lat: " + lat + " lon: " + lon + " Richtung: " + richtung + " Speed: " + speed);

						}




					});

					final Thread t = Thread.currentThread();
					Runtime.getRuntime().addShutdownHook(new Thread() {
						public void run() {
							System.out.println("\nShutting down...");
							try {
								if (serial.isOpen()) {
									serial.close();
									System.out.println("Serial port closed");
								}
								synchronized (t) {
									t.notify();
									System.out.println("Thread notified");
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
					try {
						System.out.println("Opening port [" + port + "]");
						boolean open = false;
						while (!open) {
							serial.open(port, 9600);
							open = serial.isOpen();
							System.out.println("Port is " + (open ? "" : "NOT ") + "opened.");
							if (!open)
								try {
									Thread.sleep(500L);
								} catch (Exception ex) {
								}
						}
						synchronized (t) {
							t.wait();
						}
						System.out.println("Bye...");
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}

					System.out.println();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}.start();

	}
	
	
	
	
	private void parseGPRMC(String protokoll) {
		String[] dataSplit = protokoll.split(",");
		int saetze = dataSplit.length;
		// System.out.println(Protokoll + " Sätze: " +
		// saetze);
		if (saetze > 11) {
			lat = Double.parseDouble(dataSplit[3]) / 100;
			lon = Double.parseDouble(dataSplit[5]) / 100;
			richtung = Double.parseDouble(dataSplit[8]);
			speed = Double.parseDouble(dataSplit[7]) * 1.852;
		}
	}
	
	private void parseGPGGA(String protokoll) {
		String[] dataSplit = protokoll.split(",");
		int saetze = dataSplit.length;
		// System.out.println("Sätze: " + saetze);
		if (saetze > 11) {
			// System.out.println(Protokoll + " Sätze: " +
			// saetze);

			lat = Double.parseDouble(dataSplit[2]) / 100;
			lon = Double.parseDouble(dataSplit[4]) / 100;
			alt = Double.parseDouble(dataSplit[9]) - Double.parseDouble(dataSplit[11]);
		}
	}
	

	public static int countChar(String input, char toCount) {
		int counter = 0;
		for (char c : input.toCharArray()) {
			if (c == toCount)
				counter++;
		}
		return counter;
	}

	public static void main(String args[]) throws InterruptedException {

		GPSModule gpsm = new GPSModule();
		 for (int x = 1; x < 1000; x++) {
		 System.out.println(gpsm.getAlt() + " " + gpsm.getLat() + " " +
		 gpsm.getLon() + " "
		 + gpsm.getSpeed() + " " + gpsm.getRichtung());
		 Thread.sleep(1000);
		 }
	}

	public static double getLat() {
		return lat;
	}

	public static void setLat(double lat) {
		GPSModule.lat = lat;
	}

	public static double getLon() {
		return lon;
	}

	public static void setLon(double lon) {
		GPSModule.lon = lon;
	}

	public static double getAlt() {
		return alt;
	}

	public static void setAlt(double alt) {
		GPSModule.alt = alt;
	}

	public static double getRichtung() {
		return richtung;
	}

	public static void setRichtung(double richtung) {
		GPSModule.richtung = richtung;
	}

	public static double getSpeed() {
		return speed;
	}

	public static void setSpeed(double speed) {
		GPSModule.speed = speed;
	}

}
