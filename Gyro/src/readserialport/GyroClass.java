package readserialport;



/**
 * Just reads the GPS data. No parsing, just raw data.
 */
public class GyroClass {
	public static void main(String args[]) throws InterruptedException {
		for (int i = 0; i < 5; i++) {
		SensorGyro sensor = new SensorGyro();


			Thread.sleep(50);

			//System.out.println("Angle   0: " + sensor.readAngle(0) + " /  "+ "1: " + sensor.readAngle(1) + " /  "+"2: " + sensor.readAngle(2) + "/");
			//System.out.println("---------------------------------------------------------------------------------------------------------------------");
		}
	System.exit(0);
	}
	
}