import java.util.Arrays;

public class Remembrall {
	public static boolean isPhoneNumberForgettable(String phoneNumber) {
		if(phoneNumber == null || phoneNumber.equals("")) {
			return false;
		}
		if(hasLetters(phoneNumber) || !hasRepeatingNumbers(phoneNumber)) {
			return true;
		}
		return false;
	}
	
	private static boolean hasRepeatingNumbers(String phoneNumber) {
		boolean hasHyphen = phoneNumber.contains("-");
		String delimiter;
		if(hasHyphen) {
			delimiter = "-";
		} else {
			delimiter = " ";
		}
		
		String[] numberGroups = phoneNumber.split(delimiter);
		Arrays.sort(numberGroups);
		
		for(int i = 0; i < numberGroups.length - 1; i++) {
			if(numberGroups[i].equals(numberGroups[i+1])) {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean hasLetters(String phoneNumber) {
		char[] phoneNumberArray = phoneNumber.toCharArray();
		for(char element : phoneNumberArray) {
			if(((int)element >= 65 && (int)element <= 90) || ((int)element >= 97 && (int)element <= 122)) {
				return true;
			}
		}
		return false;
	}
}
