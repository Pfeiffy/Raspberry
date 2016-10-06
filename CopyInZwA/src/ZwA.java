import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public class ZwA {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		String selectedText = "Inhaaalt_m";
		String ZwA = "System.out.println(\" " + selectedText + ": " + selectedText
				+ "           \" );";

		Toolkit.getDefaultToolkit().getSystemClipboard()
		.setContents(new StringSelection(ZwA), null);
		
		
	}
}
