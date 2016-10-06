import java.io.IOException;

import pathfinder.device.mpu6050.Mpu6050Controller;
import readserialport.SensorGyro;
import readserialport.Sensors;

/**
 * Just reads the GPS data. No parsing, just raw data.
 */
public class GyroClass {
	public static void main(String args[]) throws InterruptedException,
			IOException {
		// for (int i = 0; i < 5; i++) {
		// System.out.println("neue Version");
		// SensorGyro sensor = new SensorGyro();
		//
		// Thread.sleep(50);
		//
		// }
		// System.exit(0);

		Sensors sen = new Sensors();
		//initialize();
		while (true) {
			run();
		}

	}

	private static void run() {

	}

	private static void initialize() throws IOException, InterruptedException {
		System.out.println("Initializing Mpu6050");
		Mpu6050Controller.initialize();
		System.out.println("Mpu6050 initialized!");
	}

}