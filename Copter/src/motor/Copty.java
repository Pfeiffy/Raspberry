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
import gui.Fenster;
import gyro.Sensors;

import java.awt.EventQueue;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Copty {

	private static Scanner scanner;
	static boolean calib = false;
	static int zaehler = 0;
	static int pwm0 = 1000;
	static int pwm1 = 1000;
	static int pwm2 = 1000;
	static int pwm3 = 1000;
	static int minPWM = 1250;
	static I2CDevice deviceGyro;
	static byte hz = 0x06;
	static byte[] accelData;
	byte[] accelData0;
	byte[] accelData1;
	byte[] accelData2;
	byte[] accelData3;
	byte[] accelData4;
	byte[] accelData5;
	static double x_rotation, y_rotation = 0;
	static double y_korr = -4;

	@SuppressWarnings("resource")
	public static void main(String args[]) throws Exception {
		
				
		System.out.println("<--Pi4J--> PCA9685 PWM Example ... started.");
		BigDecimal frequency = new BigDecimal("48.828");
		BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		// Definition pwm-Verteiler
		final PCA9685GpioProvider gpioProvider = new PCA9685GpioProvider(bus,
				0x40, frequency, frequencyCorrectionFactor);
		GpioPinPwmOutput[] myOutputs = provisionPwmOutputs(gpioProvider);
		gpioProvider.reset();
		// Definition gyro
		// get device itself
		deviceGyro = bus.getDevice(0x68);
		deviceGyro.write(0x6B, (byte) 0b00000000);
		deviceGyro.write(0x6C, (byte) 0b00000000);
		deviceGyro.write(0x1C, (byte) 0b00011001);
		deviceGyro.write(0x1A, hz);

		new Thread() {
			public void run() {
				while (true) {
					try {

						getGyro();
						// Richtung x:0-2 y:1-3
						lageKorrektur();
						sleep(1000);
						gpioProvider.setPwm(PCA9685Pin.PWM_00, pwm0);
						gpioProvider.setPwm(PCA9685Pin.PWM_01, pwm1);
						gpioProvider.setPwm(PCA9685Pin.PWM_02, pwm2);
						gpioProvider.setPwm(PCA9685Pin.PWM_03, pwm3);
						//sleep(500);

					} catch (InterruptedException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}.start();

		// Eingabethread
		new Thread() {
			public void run() {
				System.out.println("Press <x> to terminate...");
				while (true) {
					try {
						scanner = new Scanner(System.in);
						String input = scanner.next();
						if (input.equalsIgnoreCase("x")) {
							pwm0 = pwm1 = pwm2 = pwm3 = 1000;
							System.exit(0);
						}

						if (countChar(input, ',') == 3) {
							String[] pwmStr = input.split(",");
							System.out.println(pwmStr[0] + " " + pwmStr[1]
									+ " " + pwmStr[2] + " " + pwmStr[3]);
							pwm0 = Integer.parseInt(pwmStr[0]) + minPWM;
							pwm1 = Integer.parseInt(pwmStr[1]) + minPWM;
							pwm2 = Integer.parseInt(pwmStr[2]) + minPWM;
							pwm3 = Integer.parseInt(pwmStr[3]) + minPWM;
							System.out.println("pwm0: " + pwm0 + " pwm1: "
									+ pwm1 + " pwm2: " + pwm2 + " pwm3: "
									+ pwm3);
						} else if (input.indexOf(",") == -1) {
							pwm0 = pwm1 = pwm2 = pwm3 = Integer.parseInt(input)
									+ minPWM;

							System.out.println("PWM(alle) : " + pwm0);
						}
					} catch (Exception e) {
						System.out.println("Fehler!!!!!");
						pwm0 = pwm1 = pwm2 = pwm3 = 1000;
						System.exit(0);
					}

				}
			}
		}.start();

	}

	//Ermittlung der lage und Korrektur pwm0 ist Master
	static void lageKorrektur() {
		double mot0,mot1,mot2,mot3 =0;
		mot0 = x_rotation;
		mot1 = -x_rotation;
		mot2 = y_rotation;
		mot3 = -y_rotation;
		
		double relativierung = 0; 
		
		
//		System.out.println("mot0: " + mot0);
//		System.out.println("mot1: " + mot1);
//		System.out.println("mot2: " + mot2);
//		System.out.println("mot3: " + mot3);
//		System.out.println("relatixiert zu mot0:");
		relativierung = 0-mot0;
		System.out.println("relatixierung:" + relativierung);
		
		mot0 = mot0 + relativierung;
		mot1 = mot1 + relativierung;
		mot2 = mot2 + relativierung;
		mot3 = mot3 + relativierung;
		System.out.println("mot0: " + (int) mot0);
		System.out.println("mot1: " + (int)mot1);
		System.out.println("mot2: " + (int)mot2);
		System.out.println("mot3: " + (int)mot3);
		pwm0 = pwm0 + (int) mot0;
		pwm1 = pwm1 + (int) mot1;
		pwm2 = pwm2 + (int) mot2;
		pwm3 = pwm3 + (int) mot3;
		System.out.println("pwm0: " + pwm0);
		System.out.println("pwm1: " + pwm1);
		System.out.println("pwm2: " + pwm2);
		System.out.println("pwm3: " + pwm3);

		

	}

	private static GpioPinPwmOutput[] provisionPwmOutputs(
			final PCA9685GpioProvider gpioProvider) {
		GpioController gpio = GpioFactory.getInstance();
		GpioPinPwmOutput myOutputs[] = {
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00,
						"Pulse 00"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01,
						"Pulse 01"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02,
						"Pulse 02"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03,
						"Pulse 03"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04,
						"Pulse 04"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05,
						"Pulse 05"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06,
						"Pulse 06"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07,
						"Pulse 07"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08,
						"Pulse 08"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09,
						"Pulse 09"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10,
						"Always ON"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11,
						"Always OFF"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12,
						"Servo pulse MIN"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13,
						"Servo pulse NEUTRAL"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14,
						"Servo pulse MAX"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15,
						"not used") };
		return myOutputs;
	}

	private static void calibMeth(PCA9685GpioProvider gpioProvider)
			throws InterruptedException {

		if (calib) {
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

	// Abfrage der Lage
	static void getGyro() throws IOException {
		accelData = new byte[6];
		int r = deviceGyro.read(0x3B, accelData, 0, 6);

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

		x_rotation = get_x_rotation(accelX_scaled, accelY_scaled, accelZ_scaled);
		y_rotation = get_y_rotation(accelX_scaled, accelY_scaled, accelZ_scaled)+y_korr;

		DecimalFormat df = new DecimalFormat("#0");

		System.out.println("--------------------");
		System.out.println("x: " + df.format(x_rotation) + " y: "
				+ df.format(y_rotation));
		System.out.println("--------------------");

	}

	static double get_y_rotation(double x, double y, double z) {
		double radians = Math.atan2(x, dist(y, z));
		return -Math.toDegrees(radians);
	}

	static double get_x_rotation(double x, double y, double z) {
		double radians = Math.atan2(y, dist(x, z));
		return Math.toDegrees(radians);
	}

	static double dist(double a, double b) {
		return Math.sqrt((a * a) + (b * b));
	}

	// *************Helperroutinen***************************
	public static int countChar(String s, char c) {
		return s.replaceAll("[^" + c + "]", "").length();
	}

	// Helper method
	private static int asInt(byte b) {
		int i = b;
		if (i < 0) {
			i = i + 256;
		}
		return i;
	}

}