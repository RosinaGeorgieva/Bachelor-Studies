package output;

import metadata.Metadata;
import mistake.Mistake;
import similarwords.SimilarityCoefficient;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public record SpellcheckOutput(String text, Metadata metadata, Set<Mistake> mistakes, Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> suggestionsByWord) { }

