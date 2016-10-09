package motor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PCA9685GpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.math.BigDecimal;
import java.util.Scanner;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider for GPIO
 * PWM pin control using the PCA9685 16-channel, 12-bit PWM I2C-bus LED/Servo
 * controller.
 * </p>
 * <p>
 * More information about the PCA9685 can be found here:<br>
 * <a href="http://www.nxp.com/documents/data_sheet/PCA9685.pdf">PCA9685.pdf</a>
 * <br>
 * <br>
 * ...and especially about the board here:<br>
 * <a href="http://www.adafruit.com/products/815">Adafruit 16-Channel 12-bit
 * PWM/Servo Driver</a>
 * </p>
 * 
 * @author Christian Wehrli
 * @see PCA9685GpioProvider
 */
public class pwmMot {

	private static Scanner scanner;
	static boolean calib = false;
	static int zaehler = 0;
	static int yaw = 1563;
	static int pitch = 1563;
	static int throttle = 1500;
	static int roll = 1563;
	static int aux1 = 1100;
	static int aux2 = 1100;
	//... und es müssen Pitch und Roll auf 1500us stehen und Throttle auf 1000 und Yaw auf 2000. 

	@SuppressWarnings("resource")
	public static void main(String args[]) throws Exception {
		System.out.println("<--Pi4J--> PCA9685 PWM Example ... started.");
		BigDecimal frequency = new BigDecimal("48.828");
		BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
		// Create custom PCA9685 GPIO provider
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		final PCA9685GpioProvider gpioProvider = new PCA9685GpioProvider(bus, 0x40, frequency,
				frequencyCorrectionFactor);
		// Define outputs in use for this example
		GpioPinPwmOutput[] myOutputs = provisionPwmOutputs(gpioProvider);
		// Reset outputs
		gpioProvider.reset();
		//
		// Set various PWM patterns for outputs 0..9
		final int offset = 400;
		final int pulseDuration = 600;

		int init = 1000;
		if (calib)
			Kalibrieren(gpioProvider);

		gpioProvider.setPwm(PCA9685Pin.PWM_00, init);
		System.out.println("PWM: " + init + " init");
		Thread.sleep(1000);
		// Set 0.9ms pulse (R/C Servo minimum position)

		new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(20);
						gpioProvider.setPwm(PCA9685Pin.PWM_00, yaw);
						gpioProvider.setPwm(PCA9685Pin.PWM_01, pitch);
						gpioProvider.setPwm(PCA9685Pin.PWM_02, throttle);
						gpioProvider.setPwm(PCA9685Pin.PWM_03, roll);
						gpioProvider.setPwm(PCA9685Pin.PWM_04, aux1);
						gpioProvider.setPwm(PCA9685Pin.PWM_05, aux2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}.start();

		System.out.println("Press <x> to terminate...");
		new Thread() {
			public void run() {
				while (true) {
					try {
						scanner = new Scanner(System.in);
						String input = scanner.next();
						if (input.equalsIgnoreCase("x")) {
							yaw = pitch = throttle = roll = aux1 = aux2 = 1000;
							System.exit(0);
						} else if (input.equalsIgnoreCase("a")) {
							yaw = 2000;
							aux1 = 1500;
							throttle = 1060;
						} else if (input.indexOf(",") != -1) {
							String[] pwms = input.split(",");
							yaw = Integer.parseInt(pwms[0]);
							pitch = Integer.parseInt(pwms[1]);
							throttle = Integer.parseInt(pwms[2]);
							roll = Integer.parseInt(pwms[3]);
							aux1 = Integer.parseInt(pwms[4]);
							aux2 = Integer.parseInt(pwms[5]);

						} else {
							yaw = pitch = throttle = roll = aux1 = aux2 = Integer.parseInt(input);
						}

						System.out.print(" pwm1: " + yaw);
						System.out.print(" pwm2: " + pitch);
						System.out.print(" pwm3: " + throttle);
						System.out.print(" pwm4: " + roll);
						System.out.print(" pwm5: " + aux1);
						System.out.print(" pwm6: " + aux2);

					} catch (Exception e) {
						System.out.println("Fehler - Abbruch");
						yaw = pitch = throttle = roll = aux1 = aux2 = 1000;
						e.printStackTrace();
						System.exit(0);
					}

				}
			}
		}.start();

	}

	private static void Kalibrieren(final PCA9685GpioProvider gpioProvider) throws InterruptedException {
		{
			System.out.println("starte kalibrierung......");
			int calib = 2000;
			gpioProvider.setPwm(PCA9685Pin.PWM_00, calib);
			gpioProvider.setPwm(PCA9685Pin.PWM_01, calib);
			gpioProvider.setPwm(PCA9685Pin.PWM_02, calib);
			gpioProvider.setPwm(PCA9685Pin.PWM_03, calib);
			Thread.sleep(10000);
			calib = 1000;
			gpioProvider.setPwm(PCA9685Pin.PWM_00, calib);
			gpioProvider.setPwm(PCA9685Pin.PWM_01, calib);
			gpioProvider.setPwm(PCA9685Pin.PWM_02, calib);
			gpioProvider.setPwm(PCA9685Pin.PWM_03, calib);
			Thread.sleep(2000);
			System.out.println("Calibrierung beendet");
			System.exit(0);

		}
	}

	private static int checkForOverflow(int position) {
		int result = position;
		if (position > PCA9685GpioProvider.PWM_STEPS - 1) {
			result = position - PCA9685GpioProvider.PWM_STEPS - 1;
		}
		return result;
	}

	private static GpioPinPwmOutput[] provisionPwmOutputs(final PCA9685GpioProvider gpioProvider) {
		GpioController gpio = GpioFactory.getInstance();
		GpioPinPwmOutput myOutputs[] = { gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Pulse 00"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Pulse 01"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Pulse 02"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Pulse 03"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Pulse 04"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Pulse 05"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Pulse 06"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Pulse 07"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Pulse 08"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Always ON"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Always OFF"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Servo pulse MIN"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Servo pulse NEUTRAL"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Servo pulse MAX"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used") };
		return myOutputs;
	}
}