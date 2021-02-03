package com.sofia.uni.fmi.ai.kmedians.alg;

import com.sofia.uni.fmi.ai.kmedians.alg.KMedians;
import com.sofia.uni.fmi.ai.kmedians.util.DataProcessor;
import com.sofia.uni.fmi.ai.kmedians.vector.Vector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Matchmaker implements Runnable {
    private static final String BIG_5 = "D:\\Desktop\\datasets\\data-final.csv"; //дали да не го направя вместо по сх по личност, по интереси?
    private static final String NAMES = "D:\\Desktop\\datasets\\names.csv";
    private static final String CODEBOOK = "D:\\Desktop\\datasets\\codebook.txt";
    private static final Map<Vector, String> dataByName = new HashMap<>();

    private static boolean isReady = false;
    private static Map<Vector, Set<Vector>> solution = new HashMap<>();
    private static List<String> codebook;

    @Override
    public void run() {
        DataProcessor dp = new DataProcessor();
//        try {
//            dp.processData(Path.of(BIG_5));
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
        List<String> observations = null;
        List<String> names = null;
        try {
            observations = dp.chooseObservations(Path.of(BIG_5));
            names = dp.chooseNames(Path.of(NAMES));
            codebook = dp.answersCodebook(Path.of(CODEBOOK));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        List<Vector> observationVectors = new ArrayList<>();
        for (int i = 0; i < observations.size(); i++) {
            Vector observationVector = new Vector(observations.get(i));
            observationVectors.add(observationVector);
            dataByName.put(observationVector, names.get(i));
            System.out.println(names.get(i));
        }

        int K = 50; //variates
        KMedians problem = new KMedians(K, observationVectors);
        try {
            problem.solve();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        solution = problem.getSolution();
        synchronized (this) {
            this.notify();
        }

        isReady = true;
    }

    public String getMatches(String user) throws IOException {
        Vector centroidOfClusterOfThisUser = null;
        Vector userData = null;
        for (Vector centroid : solution.keySet()) {
            Set<Vector> vectorsInCluster = solution.get(centroid);
            for (Vector v : vectorsInCluster) {
                if (dataByName.get(v).equals(user)) {
                    centroidOfClusterOfThisUser = centroid;
                    userData = v;
                    break;
                }
            }
        }

        if (centroidOfClusterOfThisUser == null) {
            return "No matches found!";
        }

        Map<String, Vector> otherUsersInThisCluster = new HashMap<>();
        for (Vector v : solution.get(centroidOfClusterOfThisUser)) {
            otherUsersInThisCluster.put(dataByName.get(v), v);
        }

        String outputMessage = "Description of " + user +  "'s personality: \n";
        outputMessage += allAnswersDecription(userData.getIntCoordinates());
        outputMessage += "\n";

        List<String> otherUsersNames = otherUsersInThisCluster.keySet().stream().collect(Collectors.toList());
        int randomUser = ThreadLocalRandom.current().nextInt(0, otherUsersInThisCluster.size() - 1);
        String suggestedUser = otherUsersNames.get(randomUser);
        outputMessage += "\n";
        outputMessage += "Description of " + suggestedUser + "'s personality: \n";
        outputMessage += allAnswersDecription(otherUsersInThisCluster.get(suggestedUser).getIntCoordinates());

        Path compatibilityFile = Path.of("output\\"+ user + "-" + suggestedUser + ".txt");
        Files.createFile(compatibilityFile);

        try (var printWriter = new PrintStream(new FileOutputStream(compatibilityFile.toString(), false))) {
            printWriter.println(outputMessage);
        }

        Path analysisFile = Path.of("output\\analysis1.txt");
        try (var printWriter = new PrintStream(new FileOutputStream(analysisFile.toString(), false))) {
            Set<Set<String>> clustersByNames = getClustersByNames();
            int i = 1;
            for(Set<String> cluster : clustersByNames) {
                printWriter.println("Cluster number " + i);
                i++;
                printWriter.println(cluster);
                printWriter.println();
            }
        }

        return suggestedUser;
    }

    public boolean isReady() {
        return this.isReady;
    }

    public Set<Set<String>> getClustersByNames() {
        Set<Set<String>> clustersByNames = new HashSet<>();
        for(Vector centorid : solution.keySet()) {
            Set<String> clusterNames = new HashSet<>();
            for(Vector vector : solution.get(centorid)) {
                clusterNames.add(dataByName.get(vector));
            }
            clustersByNames.add(clusterNames);
        }
        return clustersByNames;
    }

    private String allAnswersDecription(List<Integer> answers) {
        String description = "";
        for (int i = 0; i < answers.size(); i++) {
            description += codebook.get(i);
            description += "->";
            switch (answers.get(i)) {
                case 1:
                    description += "inaccurate";
                    break;
                case 2:
                    description += "inaccurate to neutral";
                    break;
                case 3:
                    description += "neutral";
                    break;
                case 4:
                    description += "neutral to accurate";
                    break;
                case 5:
                    description += "accurate";
                    break;
            }
            description += '\n';
        }
        return description;
    }

}
