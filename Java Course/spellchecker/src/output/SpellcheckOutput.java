package output;

import mistake.Mistake;
import spellchecker.Metadata;

import java.util.*;

public record SpellcheckOutput(String text, Metadata metadata, List<Mistake> mistakes,
                               Map<String, ArrayList<String>> suggestionsByWord) {
}

