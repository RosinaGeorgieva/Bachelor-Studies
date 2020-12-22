package output;

import dictionaries.Dictionary;
import exceptions.Exceptions;
import exceptions.IOException;
import mistake.Mistake;
import similarwords.SimilarWordsDictionary;

import java.io.*;
import java.util.Collection;
import java.util.List;

//ne e refactored sys stream api
public class SpellcheckOutputFormatter {
    private static final String DELIMITER = "= = =";
    private static final String METADATA = " Metadata ";
    private static final String FINDINGS = " Findings ";
    private static final String NEWLINE = "\n";
    private static final String CHARACTERS = " characters, ";
    private static final String WORDS = " words, ";
    private static final String MISTAKES = " spelling issue(s) found";
    private static final String LINE_NUMBER = "Line #";
    private static final String BEGINNING_OF_MISTAKEN_WORD = ", {";
    private static final String END_OF_MISTAKEN_WORD = "}";
    private static final String BEGINNING_OF_SUGGESTED_WORDS = " - Possible suggestions are {";
    private static final String END_OF_SUGGESTED_WORDS = "}\n";
    private static final String COMMA = ", ";


    public static void generate(SpellcheckOutput spellCheckerResult, int n, Writer output, Dictionary dictionary, SimilarWordsDictionary similarWordsDictionary) {
        write(spellCheckerResult.text(), output);
        try {
            output.append(NEWLINE + DELIMITER + METADATA + DELIMITER + NEWLINE);
            output.append(metadataSummary(spellCheckerResult));
            output.append(NEWLINE + DELIMITER + FINDINGS + DELIMITER + NEWLINE);
            output.append(findingsSummary(spellCheckerResult, n, dictionary, similarWordsDictionary));
        } catch (java.io.IOException exception) {
            throw new IOException(Exceptions.IO_EXCEPTION.getMessage());
        }
    }

    private static String findingsSummary(SpellcheckOutput spellCheckerResult, int n, Dictionary dictionary, SimilarWordsDictionary similarWordsDictionary) {
        String summary = new String();
        Collection<Mistake> mistakes = spellCheckerResult.mistakes();
        if (mistakes.size() != 0) {
            for (Mistake mistake : mistakes) {
                summary = summary.concat(LINE_NUMBER);
                summary = summary.concat(String.valueOf(mistake.line()));
                summary = summary.concat(BEGINNING_OF_MISTAKEN_WORD);
                summary = summary.concat(mistake.word());
                summary = summary.concat(END_OF_MISTAKEN_WORD);
                summary = summary.concat(BEGINNING_OF_SUGGESTED_WORDS);
                List<String> suggestedWords = similarWordsDictionary.getNMostSimilar(mistake.word(), n, dictionary);
                for (int i = 0; i < n; i++) {
                    summary = summary.concat(suggestedWords.get(i));
                    if (i != n - 1) {
                        summary = summary.concat(COMMA);
                    }
                }
            }
            summary = summary.concat(END_OF_SUGGESTED_WORDS);
        }
        return summary;
    }

    private static String metadataSummary(SpellcheckOutput spellCheckerResult) { //super bavno, zashtoto 2 pyti suzdavam metadata
        return spellCheckerResult.metadata().characters() + CHARACTERS + spellCheckerResult.metadata().words() + WORDS + spellCheckerResult.metadata().mistakes() + MISTAKES;
    }

    private static void write(String text, Writer writer) { //imeto e tupo
        try ( var stringWriter = new BufferedWriter(writer)) { //???????????/
            stringWriter.write(text);
            stringWriter.flush();
        } catch (java.io.IOException exception) {
            throw new IOException(Exceptions.IO_EXCEPTION.getMessage());
        }
    }
}
