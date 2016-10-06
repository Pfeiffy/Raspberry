package Test;

import java.util.ArrayList;
import java.util.Collections;

public class LageTest {
	static double x_rotation, y_rotation = 0;

	public static void main(String[] args) {
		x_rotation = -2;
		y_rotation = 3;

		//Höhe über Mitte errechnen
		double mot0,mot1,mot2,mot3 =0;
		mot0 = x_rotation;
		mot1 = -x_rotation;
		mot2 = y_rotation;
		mot3 = -y_rotation;
		double relativierung = 0; 
		
		
		System.out.println("mot0: " + mot0);
		System.out.println("mot1: " + mot1);
		System.out.println("mot2: " + mot2);
		System.out.println("mot3: " + mot3);
		System.out.println("relatixiert zu mot0:");
		relativierung = 0-mot0;
		System.out.println("relatixierung:" + relativierung);
		
		mot0 = mot0 + relativierung;
		mot1 = mot1 + relativierung;
		mot2 = mot2 + relativierung;
		mot3 = mot3 + relativierung;
		System.out.println("mot0: " + mot0);
		System.out.println("mot1: " + mot1);
		System.out.println("mot2: " + mot2);
		System.out.println("mot3: " + mot3);

		// wenn die Seite nach oben geht
		// 0 x+
		// 1 y-
		// 2 x-
		// 3 y+
		// 1. 8 Fälle ermitteln

		// 1. Fall beide > 0 --> 2,1 erhöhen
		if (x_rotation > 0 && y_rotation > 0) {
			if (x_rotation > y_rotation) {

			}
			if (x_rotation < y_rotation) {

			}
			if (x_rotation == y_rotation) {

			}

		}

		// 2. Fall beide < 0
		if (x_rotation < 0 && y_rotation < 0) {

		}
		// 3. x>0, y<0
		if (x_rotation > 0 && y_rotation < 0) {

		}
		// 4. x<0, <0
		if (x_rotation < 0 && y_rotation > 0) {

		}
		// 5. x=0,y>0
		if (x_rotation == 0 && y_rotation > 0) {

		}
		// 7. x=0,y<0
		if (x_rotation == 0 && y_rotation < 0) {

		}
		// 8. x<0,y=0
		if (x_rotation < 0 && y_rotation == 0) {

		}
		// 5. x>0,y=0
		if (x_rotation > 0 && y_rotation == 0) {

		}

		ArrayList<Double> propHoehen = new ArrayList<Double>();

		Collections.sort(propHoehen);

		System.out.println(propHoehen);

	}

}
