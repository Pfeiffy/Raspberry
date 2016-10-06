package readserialport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GPSBer {

	public static double lat = 0;
	public static double lon = 0;
	public static double alt = 0;
	public static double richtung = 0;
	public static double speed = 0;

	public static void main(String[] args) throws IOException {

		BufferedReader f;

		String dataAkt = "";
		String dataVor = "";
		String data = " ";

		try {
			f = new BufferedReader(new FileReader("gpsdata.txt"));
			while ((dataAkt = f.readLine()) != null) {
				int pos = dataAkt.indexOf('$');
				if (pos > -1) {
					// Verarbeitung Datavor, wenn neues Protokoll kommt;
					System.out.println("verarbeitungsdata: " + dataVor);
					String Protokoll = dataVor.substring(pos, pos + 6);
					System.out.println("Protokoll: " + dataAkt.substring(pos, pos + 6));
					if (Protokoll.equalsIgnoreCase("$GPRMC")) {
						String[] dataSplit = dataVor.split(",");
						int saetze = dataSplit.length;
						System.out.println(Protokoll + " Sätze: " + saetze);
						try {

							lat = Double.parseDouble(dataSplit[3]) / 100;
							lon = Double.parseDouble(dataSplit[5]) / 100;
							richtung = Double.parseDouble(dataSplit[8]);
							speed = Double.parseDouble(dataSplit[7])*1.852;

							System.out.println("lat: " + lat + "  lon: " + lon + "  Richtung: " + richtung+ "  Speed: "+ speed);
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
					if (Protokoll.equalsIgnoreCase("$GPGGA")) {
						String[] dataSplit = dataVor.split(",");
						int saetze = dataSplit.length;
						System.out.println(Protokoll + " Sätze: " + saetze);
						try {

							lat = Double.parseDouble(dataSplit[2]) / 100;
							lon = Double.parseDouble(dataSplit[4]) / 100;
							alt = Double.parseDouble(dataSplit[9]) - Double.parseDouble(dataSplit[11]);

							System.out.println(
									"lat: " + lat + " lon: " + lon + " Richtung: " + richtung + " Höhe:" + alt);
						} catch (Exception e) {
							// TODO: handle exception
						}

					}

					data = dataAkt;
				} else
				// vorheriges protokoll war nicht fertig
				{
					data = dataVor + dataAkt;
				}

				dataVor = data;

			}
		} catch (FileNotFoundException e) {

		}

	}

}
