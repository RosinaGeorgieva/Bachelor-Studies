package util;

import java.util.*;

public class WordCleanser {
    public static String makeCaseInsensitive(String text){
        return text.toLowerCase();
    }

    public static String removeBorderWhitespaces(String text){
        return text.trim();
    }

    public static String removeBorderNonAlphanumeric(String text){
        return text.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", " ").trim();
    }

    public static String[] removeAllWhitespaces(String text){
        List<String> stripped = new ArrayList<>();
        String[] words = text.split(" ");
        for(String word : words){
            word = word.replaceAll("\\s+","");
            if(!word.equals("")){
                stripped.add(word);
            }
        }
        return  stripped.toArray(new String[0]);
    }
}
