package kompas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Kompass {
	static I2CBus bus = null;
	static I2CDevice Hmc5883l = null;
	static int loops = 10;
	static double scale = 0.92;

	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println("Winkel: " + getWinkel());

		getWinkel10();
	}

	public static void getWinkel10() throws IOException, InterruptedException {
		List<Double> list = new ArrayList<Double>();

		for (int x = 0; x < loops; x++) {
			list.add(getWinkel());
		}

		// System.out.println(heading);
		// }Collections.sort(list);
		//
		Collections.sort(list);
		double returnWinkel = 0;
		for (int y = 2; y < list.size() - 2; y++) {
			returnWinkel = returnWinkel + list.get(y);
			System.out.println(list.get(y) + " ReturnWinkel: " + returnWinkel);
		}
		//
		System.out.println("Ergebnis: " + (returnWinkel / (list.size() - 4)));
	}

	private static double getWinkel() throws IOException, InterruptedException {
		bus = I2CFactory.getInstance(I2CBus.BUS_1);
		Hmc5883l = bus.getDevice(0x1e);

		double heading = 0;
		Hmc5883l.write("0b01110000".getBytes(), 0, "0b01110000".length());
		Hmc5883l.write("0b00100000".getBytes(), 1, "0b01110000".length());
		Hmc5883l.write("0b00000000".getBytes(), 2, "0b01110000".length());

		double x_out = read(3, Hmc5883l) * scale;
		double y_out = read(7, Hmc5883l) * scale;
		double z_out = read(5, Hmc5883l) * scale;

		// System.out.println("X: " + x_out + " Y: " + y_out + " Z: " +
		// z_out);
		heading = Math.atan2(y_out, x_out);
		if (heading < 0) {
			heading += (2 * Math.PI);
		}
		heading = Math.toDegrees(heading);

		return heading;

	}

	private static double read(int add, I2CDevice i2c) throws IOException {
		int val1 = i2c.read(add);
		int val2 = i2c.read(add + 1);
		int val = (val1 << 8) + val2;

		if (val >= 0x8000) {
			return -((65535 - val) + 1);
		} else {
			return val;
		}

	}

}
