package Helper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Helper {

	// ----------------------------------
	public static void schreiben(String sLine, String outputFile, boolean anhaengen) {
		// ----------------------------------
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFile, anhaengen)));
			out.write(sLine);
			out.newLine();
			out.close();
		} catch (Exception e) {
			System.err.println(e.toString());
			System.exit(1);
		}
	}

}
