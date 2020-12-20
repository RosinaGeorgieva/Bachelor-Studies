package util;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public record SpellCheckerResult(Reader text, Set<Mistake> mistakes, Map<String, TreeMap<Double, ArrayList<String>>> suggestionsByWord) { }

