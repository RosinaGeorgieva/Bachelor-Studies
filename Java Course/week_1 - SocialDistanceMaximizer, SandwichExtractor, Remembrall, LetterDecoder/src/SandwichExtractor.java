import java.util.Arrays;

public class SandwichExtractor {
	public static String[] extractIngredients(String sandwich) {
		int beginningOfSandwich = sandwich.indexOf("bread");
		int endOfSandwich = sandwich.lastIndexOf("bread");
		
		if(beginningOfSandwich == endOfSandwich) {
			return new String[] {};
		}
		
		beginningOfSandwich += "bread".length();

		String[] contents = sandwich.substring(beginningOfSandwich, endOfSandwich).split("-");
		
		int nonOliveIngredients = 0;
		for(int i = 0; i < contents.length; i++) {
			if(!contents[i].equals("olives")) {
				nonOliveIngredients++;
			}
		}
		String[] contentsWithoutOlives = new String[nonOliveIngredients];
		
		int lastIndex = 0;
		for(int i = 0; i < contents.length; i++) {
			if(!contents[i].equals("olives")) {
				contentsWithoutOlives[lastIndex] = contents[i];
				lastIndex++;
			}
		}
		Arrays.sort(contentsWithoutOlives);
		
		return contentsWithoutOlives;
	}
}
