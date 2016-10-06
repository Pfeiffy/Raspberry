package readserialport;

//dies kommt von der Seite: https://blogs.oracle.com/acaicedo/entry/beyond_beauty_javafx_i2c_parallax
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Sensors {

	I2CBus bus;
	I2CDevice device;
	byte[] accelData, accelData0, accelData1, accelData2, accelData3,
			accelData4, accelData5;
	static String logFile = "outGyro.txt";
	byte hz = 0x06;
	//String hz = "9999";

	public Sensors() {
		System.out.println("Starting sensors reading:");
		// get I2C bus instance
		try {
			// get i2c bus
			bus = I2CFactory.getInstance(I2CBus.BUS_1);
			System.out.println("Connected to bus OK!");

			// get device itself
			device = bus.getDevice(0x68);
			System.out.println("Connected to device OK!");

			// start sensing, using config registries 6B and 6C
			device.write(0x6B, (byte) 0b00000000);
			device.write(0x6C, (byte) 0b00000000);
			System.out.println("Configuring Device OK!");

			// config accel
			device.write(0x1C, (byte) 0b00011001);
			System.out.println("Configuring sensors OK!");

			// HZ einstellen
			device.write(0x1A, hz);

			// StatusMessage(Write(new byte[] { 0x1A, 0x06 }));

			startReading();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	// Create a separate thread for reading the sensors

	public void startReading() {

		new Thread() {
			public void run() {
				try {

					readingSensors();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Thread Running");
			}
		}.start();

	}

	private void readingSensors() throws IOException, InterruptedException {
		int z = 0;
		int ende = 1;
		while (z < ende) {
			z++;
			int zaehler = 100;
			double xDoubles = 0;
			double yDoubles = 0;
			long timeV = System.currentTimeMillis();
			// alle 6 Register auf einmal lesen
			for (int x = 0; x < zaehler; x++) {

				accelData = new byte[6];
				int r = device.read(0x3B, accelData, 0, 6);
				// System.out.println("------------Test----------");
				// System.exit(0);

				if (r != 6) {
					System.out.println("Error reading accel data, < 6 bytes");
				}
				// Convert the values to integers, using the
				// helper method asInt
				double accelX = asInt(accelData[0]) * 256 + asInt(accelData[1]);
				if (accelX >= 32768) {
					accelX = -((65535 - accelX) + 1);
				}
				double accelY = asInt(accelData[2]) * 256 + asInt(accelData[3]);
				if (accelY >= 32768) {
					accelY = -((65535 - accelY) + 1);
				}
				double accelZ = asInt(accelData[4]) * 256 + asInt(accelData[5]);
				if (accelZ >= 32768) {
					accelZ = -((65535 - accelZ) + 1);
				}

				double accelX_scaled = accelX / 16384;
				double accelY_scaled = accelY / 16384;
				double accelZ_scaled = accelZ / 16384;

				double x_rotation = get_x_rotation(accelX_scaled,
						accelY_scaled, accelZ_scaled);
				double y_rotation = get_y_rotation(accelX_scaled,
						accelY_scaled, accelZ_scaled);

				xDoubles = xDoubles + x_rotation;
				yDoubles = yDoubles + y_rotation;

			}
			// durch den Zähler Teilen, um das ar. Mittel zu bekommen
			xDoubles = xDoubles / zaehler;
			yDoubles = yDoubles / zaehler;
			// System.exit(0);

			DecimalFormat df = new DecimalFormat("#0.00");

			System.out.println("Mittel x: " + df.format(xDoubles));
			System.out.println("Mittel y: " + df.format(yDoubles));
			long benZeit = System.currentTimeMillis() - timeV;
			System.out.println("Benötigte Zeit: (ms)" + (benZeit));
			Helper.Helper.schreiben(
					df.format(xDoubles) + ";" + df.format(yDoubles) + ";" + hz
							+ ";" + benZeit, logFile, true);

			Thread.sleep(200);
		}
	}

	double get_y_rotation(double x, double y, double z) {
		double radians = Math.atan2(x, dist(y, z));
		return -Math.toDegrees(radians);
	}

	double get_x_rotation(double x, double y, double z) {
		double radians = Math.atan2(y, dist(x, z));
		return Math.toDegrees(radians);
	}

	double dist(double a, double b) {
		return Math.sqrt((a * a) + (b * b));
	}

	// Helper method
	private static int asInt(byte b) {
		int i = b;
		if (i < 0) {
			i = i + 256;
		}
		return i;
	}
	
	
//	 public static void main(String[] args) throws IOException, InterruptedException {
//	 Sensors sen = new Sensors();
//	 }
//	 }
	
}
