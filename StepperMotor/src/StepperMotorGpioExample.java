/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  StepperMotorGpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * This example code demonstrates how to control a stepper motor using the GPIO
 * pins on the Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class StepperMotorGpioExample {

	public static void main(String[] args) throws InterruptedException {

		System.out
				.println("<--Pi4J--> GPIO Stepper Motor Example ... started.");

		// create gpio controller
		final GpioController gpio = GpioFactory.getInstance();

		// provision gpio pins #00 to #03 as output pins and ensure in
		// LOW state
		final GpioPinDigitalOutput[] pins = {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW) };

		final GpioPinDigitalOutput[] pins1 = {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW) };
		// this will ensure that the motor is stopped when the program
		// terminates
		gpio.setShutdownOptions(true, PinState.LOW, pins);
		gpio.setShutdownOptions(true, PinState.LOW, pins1);

		// create motor component
		GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);
		GpioStepperMotorComponent motor1 = new GpioStepperMotorComponent(pins1);

		byte[] single_step_sequence = new byte[4];
		single_step_sequence[0] = (byte) 0b0001;
		single_step_sequence[1] = (byte) 0b0010;
		single_step_sequence[2] = (byte) 0b0100;
		single_step_sequence[3] = (byte) 0b1000;
		//

		motor.setStepInterval(2);
		motor.setStepSequence(single_step_sequence);
		motor.setStepsPerRevolution(2038);

		motor1.setStepInterval(2);
		motor1.setStepSequence(single_step_sequence);
		motor1.setStepsPerRevolution(2038);
		//

		new Thread() {
			public void run() {
				long start = System.nanoTime();
				System.out.println("Motor 0 startet");
				for (int x = 1; x < 1000; x++) {
					motor.step(3);
				}

				long end = System.nanoTime();
				System.out.println("Laufzeit: " + (end - start) / 100000);
				motor.stop();
				System.out.println("Motor 0 wurde gestoppt.");

			}
		}.start();

		new Thread() {
			public void run() {
				try {
					System.out.println("Motor 1 startet  in 1 Sec");
					Thread.sleep(4000);
					long start = System.nanoTime();
					System.out.println("Motor 1 startet!");
					for (int x = 1; x < 1000; x++) {
						motor1.step(3);
					}

					long end = System.nanoTime();
					System.out
							.println("Laufzeit 1 : " + (end - start) / 100000);
					motor1.stop();
					System.out.println("Motor 1 wurde gestoppt.");

				} catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();

		//
		// new Thread() {
		// @Override
		// public void run() {
		//
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println("nun müsste er stoppen");
		// //motor.stop();
		// System.out.println("Motor 0 wurde gestoppt.");
		// gpio.shutdown();
		// }
		// }.start();

	}
}