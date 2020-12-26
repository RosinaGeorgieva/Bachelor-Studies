package output;

import mistake.Mistake;

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class SpellcheckOutputFormatter {
    private static final String DELIMITER = "= = =";
    private static final String COMMA = ", ";
    private static final String FINDINGS_FORMAT = "Line #%d, {%s} - Possible suggestions are {%s}";
    private static final String NO_SUGGESTIONS = "No suggestions found.";

    public static void format(SpellcheckOutput spellCheckerOutput, Writer output) {
        Formatter formatter = new Formatter(output);
        formatter.format("%s%n", spellCheckerOutput.text());
        formatter.flush();
        formatter.format("%s Metadata %s%n", DELIMITER, DELIMITER);
        formatter.flush();
        formatter.format("%s%n", spellCheckerOutput.metadata().toString());
        formatter.flush();
        formatter.format("%s Findings %s%n", DELIMITER, DELIMITER);
        formatter.flush();
        formatter.format("%s", formatSuggestions(spellCheckerOutput));
        formatter.close();
    }

    private static String formatSuggestions(SpellcheckOutput spellcheckOutput) {
        String suggestionsFormatted = new String();
        if (spellcheckOutput.mistakes().size() != 0) {
            List<String> findingsLines = new ArrayList<>();
            for (Mistake mistake : spellcheckOutput.mistakes()) {
                Formatter formatter = new Formatter();
                String allSuggestions = String.join(COMMA, new ArrayList<>(spellcheckOutput.suggestionsByWord().get(mistake.word())));
                formatter.format(FINDINGS_FORMAT, mistake.line(), mistake.word(), allSuggestions);
                findingsLines.add(formatter.toString());
            }
            suggestionsFormatted = String.join(System.lineSeparator(), findingsLines);
        } else {
            suggestionsFormatted = NO_SUGGESTIONS;
        }
        return suggestionsFormatted;
    }
}
