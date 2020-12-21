package wordcleanser;

import java.util.*;

public class WordNormalizer { //imeto na paketa, literalite ...
    public static String normalize(String word){
        word = WordNormalizer.makeCaseInsensitive(word);
        word = WordNormalizer.removeBorderWhitespaces(word);
        return WordNormalizer.removeBorderNonAlphanumeric(word);
    }

    public static String makeCaseInsensitive(String text){
        return text.toLowerCase();
    }

    public static String removeBorderWhitespaces(String text){
        return text.trim();
    }

    public static String removeBorderNonAlphanumeric(String text){//DA GO NAPRAWQ SYS STRING

        return text.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
    }

    public static String[] removeAllWhitespaces(String text){ //ne mu e mqstoto tuk
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
