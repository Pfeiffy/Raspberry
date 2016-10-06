package test;

import java.io.BufferedReader;
import java.io.FileReader;

public class datLes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader f;
		String line;

		while (true) {
			try {
				f = new BufferedReader(new FileReader("test.dat"));
				while ((line = f.readLine()) != null) {

					System.out.println("Text:  " + line);

				}
				f.close();

			} catch (Exception e) {
				System.out.println("Fehler beim Lesen der Datei");
			}
		}

	}

}
