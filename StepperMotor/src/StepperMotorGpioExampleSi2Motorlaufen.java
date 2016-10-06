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
public class StepperMotorGpioExampleSi2Motorlaufen {

	public static void main(String[] args) throws InterruptedException {

		// funktioniert mit folgender PIN-belegung:
		// PIN-Farben wie an Motor dann:
		// organe auf 11 (GPIO0) 	--> IN4
		// gelb auf 12 (GPIO1) 		--> IN3
		// lila/rosa auf 13 (GPIO2) --> IN2
		// blau auf 15 (GPIO3) 		-->IN1
		System.out
				.println("<--Pi4J--> GPIO Stepper Motor Example ... started.");

		// create gpio controller
		final GpioController gpio = GpioFactory.getInstance();

		// provision gpio pins #00 to #03 as output pins and ensure in LOW state
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

		// @see http://www.lirtex.com/robotics/stepper-motor-controller-circuit/
		// for additional details on stepping techniques

		// create byte array to demonstrate a single-step sequencing
		// (This is the most basic method, turning on a single electromagnet
		// every time.
		// This sequence requires the least amount of energy and generates the
		// smoothest movement.)
		byte[] single_step_sequence = new byte[4];
		single_step_sequence[0] = (byte) 0b0001;
		single_step_sequence[1] = (byte) 0b0010;
		single_step_sequence[2] = (byte) 0b0100;
		single_step_sequence[3] = (byte) 0b1000;


		// define stepper parameters before attempting to control motor
		// anything lower than 2 ms does not work for my sample motor using
		// single step sequence
		motor.setStepInterval(2);
		motor.setStepSequence(single_step_sequence);
		motor1.setStepInterval(2);
		motor1.setStepSequence(single_step_sequence);

		// There are 32 steps per revolution on my sample motor, and inside is a
		// ~1/64 reduction gear set.
		// Gear reduction is actually:
		// (32/9)/(22/11)x(26/9)x(31/10)=63.683950617
		// This means is that there are really 32*63.683950617 steps per
		// revolution = 2037.88641975 ~ 2038 steps!
		motor.setStepsPerRevolution(2038);
		motor1.setStepsPerRevolution(2038);

		// test motor control : STEPPING FORWARD
		System.out.println("   Motor FORWARD for 2038 steps.");
		motor1.step(2038);
		motor1.stop();
		motor.step(2038);
		motor.stop();
		System.out.println("   Motor STOPPED.");

		// final stop to ensure no motor activity
//		motor1.stop();
	

		// stop all GPIO activity/threads by shutting down the GPIO controller
		// (this method will forcefully shutdown all GPIO monitoring threads and
		// scheduled tasks)
		gpio.shutdown();
	}
}