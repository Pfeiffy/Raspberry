package test;

import java.io.IOException;
import java.util.*;

public class Eingabe {
	private Scanner scanner;
	static int zaehler = 0;

	public Eingabe() {
		new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Zaehler: " + zaehler);
				}
			}
		}.start();

		scanner = new Scanner(System.in);
		boolean quit = false;
		new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (scanner.next().equals("1")) {
						zaehler++;
					}
					if (scanner.next().equals("2")) {
						zaehler--;
					}

				}
			}
		}.start();

	}

	public static void main(String args[]) {
		Eingabe e = new Eingabe();
	}

}
