public class LetterDecoder {

    @SuppressWarnings("deprecation")
	public String decodeMessage(final String message) {
        String month = getMonth(message);
     //   int date = getDate(message);
        int dateInNumber = getDate(message);
        String suffix = "th";
        if (dateInNumber % 10 == 1 && dateInNumber != 11) {
        	suffix = "st";
        } else if (dateInNumber % 10 == 2 && dateInNumber != 12) {
        	suffix = "nd";
        } else if (dateInNumber % 10 == 3 && dateInNumber != 13) {
        	suffix = "rd";
        }
        String date = Integer.valueOf(dateInNumber).toString() + suffix;
        
        int hour = getHour(message);
        if(hour > 24) {
        	System.out.println("Maybe another hour.");
        	return null;
        }
        int minutes = getMinutes(message);
        if(minutes > 60) {
        	System.out.println("Maybe another minute.");
        	return null;
        }
        return "[" + date + " " + month + " at " + new Integer(hour).toString() + ":" + new Integer(minutes).toString() + "]";
    }

    private String getMonth(final String message) {
    	char[] result = new char[3];
    	int i = 0;
    	char[] messageArray = message.toCharArray();
    	for(char symbol : messageArray) {
    		if (((symbol >= 65 && symbol <= 90 ) || (symbol >= 97 && symbol <= 122)) && i < 3) {
    			result[i] = symbol;
    			i++;
    		}
    	}
    	return result.toString();
    }
    
    private int getDate(final String message) {
    	int firstIndex = message.indexOf("The");
    	int lastIndex = message.indexOf(".", firstIndex);
    	return message.substring(firstIndex, lastIndex).length() + 1;
    }
    
    private int getHour(final String message) {
    	int punctuationMistakesCounter = 0;
    	char[] messageArray = message.toCharArray();
    	for(int i = 0; i < messageArray.length - 1 ; i++) {
    		if((messageArray[i] == ' ' && messageArray[i+1] >= 65 && messageArray[i+1] <= 90) || (messageArray[i] == '.' && messageArray[i+1] == '.')) {
    			punctuationMistakesCounter++;
    		}
    	}
    	return punctuationMistakesCounter;
    }
    
    private int getMinutes(final String message) {
    	int writingMistakesCounter = 0;
    	char[] messageArray = message.toCharArray();
    	for(char symbol : messageArray) {
    		if(!(symbol >= 65 && symbol <= 90 ) || (symbol >= 97 && symbol <= 122)) {
    			writingMistakesCounter++;
    		}
    	}
    	return writingMistakesCounter;
    }
}