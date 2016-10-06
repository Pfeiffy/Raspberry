package Helper;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String input = "12#11#13#14";
		// wenn mehrere Motoren angesprochen werden
		int plusse = countChar(input, '#');
		System.out.println("Plusse: " + plusse);
		if (plusse == 3) {
			String[] pwmStr = input.split("#");
			System.out.println(pwmStr);
		}
	}

	public static int countChar(String s, char c) {
		return s.replaceAll("[^" + c + "]", "").length();
	}

}
