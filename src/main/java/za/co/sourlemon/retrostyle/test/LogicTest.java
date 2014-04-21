package za.co.sourlemon.retrostyle.test;

public class LogicTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(
				"!a || (a^b) :\n\n" +
				" a | b | o \n" +
				"-----------\n" +
				" 1 | 1 | " + alpha(true,true) + "\n" +
				" 1 | 0 | " + alpha(true,false) + "\n" +
				" 0 | 1 | " + alpha(false,true) + "\n" +
				" 0 | 0 | " + alpha(false,false) + "\n");
		
		System.out.println(
				"\n!(a && !b) :\n\n" +
				" a | b | o \n" +
				"-----------\n" +
				" 1 | 1 | " + beta(true,true) + "\n" +
				" 1 | 0 | " + beta(true,false) + "\n" +
				" 0 | 1 | " + beta(false,true) + "\n" +
				" 0 | 0 | " + beta(false,false) + "\n");
		
	}

	static boolean alpha(boolean a, boolean b) {
		return !a || (a^b);
	}
	
	static boolean beta(boolean a, boolean b) {
		return !(a && !b);
	}
}
