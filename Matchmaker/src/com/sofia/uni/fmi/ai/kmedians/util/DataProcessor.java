package com.sofia.uni.fmi.ai.kmedians.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataProcessor {
    private static final String HELPER = "D:\\Desktop\\datasets\\helper.csv";
    private static final String EMPTY_ANSWER = ",0,";
    private static final String HEADER = "EXT";
    private static final String HEADER_NAMES = "Name";
    private static final String SPACE = "\\s+";
    private static final String DOUBLE_DOT = ":";
    private static final String COMMA = ",";

    public List<String> answersCodebook(Path filePath) throws IOException {
        Stream<String> lines = Files.lines(filePath);

        List<String> codebook = new ArrayList<>();
        lines.map(line -> line.split(DOUBLE_DOT)[1]).forEach(line -> codebook.add(line));

        return codebook;
    }

    public List<String> chooseNames(Path filePath) throws IOException {
        Stream<String> lines = Files.lines(filePath);

        Set<String> names = new HashSet<>();

        lines.filter(line -> !line.contains(HEADER_NAMES))
                .map(line -> line.split(COMMA)[1])
                .forEachOrdered(line -> names.add(line));

        return new ArrayList<>(names);
    }

    public List<String> chooseObservations(Path filePath) throws IOException {
        Stream<String> lines = Files.lines(filePath);

        List<String> observations = new ArrayList<>();
        lines.filter(line -> !line.contains(HEADER)).forEachOrdered(observations::add);

        return observations;
    }

    public void processData(Path filePath) throws IOException {
        Stream<String> lines = Files.lines(filePath);

        Path fileWithoutEmptyAnswers = Path.of(HELPER);
        try (var printWriter = new PrintStream(new FileOutputStream(fileWithoutEmptyAnswers.toString(), false))) {
            lines.filter(line -> !line.contains(EMPTY_ANSWER))
                    .collect(Collectors.toList())
                    .subList(0, 5001).stream()
                    .map(DataProcessor::stringWithCommas)
                    .map(DataProcessor::stripAdditionalColumns)
                    .map(DataProcessor::stripSpaces)
                    .forEachOrdered(printWriter::println);
        }

        Files.move(fileWithoutEmptyAnswers, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private static String stripSpaces(String string) {
        return string.replaceAll(SPACE, "");
    }

    private static String stringWithCommas(String string) {
        return string.replaceAll(SPACE, COMMA);
    }

    private static String stripAdditionalColumns(String string) {
        String result = Arrays.toString(Arrays.copyOfRange(string.split(COMMA), 0, 50));
        return result.substring(1, result.length() - 1);
    }
}
