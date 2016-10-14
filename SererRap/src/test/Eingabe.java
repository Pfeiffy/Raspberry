package test;

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
		new Thread() {
			public void run() {
				while (true) {
					scanner = new Scanner(System.in);
					String input = scanner.next();
					if (input.equals("1")) {
						zaehler++;
					}
					if (input.equals("2")) {
						zaehler--;
					}
					System.out.println(zaehler);
				}
			}
		}.start();
	}

	public static void main(String args[]) {
		Eingabe e = new Eingabe();
	}
}
