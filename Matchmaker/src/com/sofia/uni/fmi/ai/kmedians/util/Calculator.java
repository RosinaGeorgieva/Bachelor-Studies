package com.sofia.uni.fmi.ai.kmedians.util;

import com.sofia.uni.fmi.ai.kmedians.vector.Vector;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Calculator {
    public double calculateManhattanDistance(Vector firstVector, Vector secondVector) {
        double manhattanDist = 0;
        List<Double> firstVectorCoordinates = firstVector.getCoordinates();
        List<Double> secondVectorCoordinates = secondVector.getCoordinates();
        for (int i = 0; i < firstVectorCoordinates.size(); i++) {
            manhattanDist += Math.abs(firstVectorCoordinates.get(i) - secondVectorCoordinates.get(i));
        }

        return manhattanDist;
    }

    public double calculateAverageSilhouetteCoefficient(Map<Vector, Set<Vector>> clusters) {
        if (clusters.keySet().size() == 1) {
            return 0.0;
        }

        double sum = 0.0;
        int sampleSize = 0;
        for (Vector centroid : clusters.keySet()) {
            Set<Vector> vectors = clusters.get(centroid);
            for (Vector vi : vectors) {
                sum += calculateSilhouetteCoefficient(clusters, centroid, vi);
                sampleSize++;
            }
        }
        return sum / sampleSize;
    }

    private double calculateSilhouetteCoefficient(Map<Vector, Set<Vector>> clusters, Vector centroid, Vector current) {
        double x = meanDistanceToOthers(clusters.get(centroid), current);
        double y = meanNearestClusterDistance(clusters, centroid, current);

        return (x - y) / Math.max(x, y);
    }

    private double meanDistanceToOthers(Set<Vector> vectors, Vector current) {
        double distance = 0;
        for (Vector vj : vectors) {
            if (!current.equals(vj)) {
                distance += calculateManhattanDistance(current, vj);
            }
        }
        distance /= vectors.size() - 1;
        return distance;
    }

    //single linkage
    private double meanNearestClusterDistance(Map<Vector, Set<Vector>> clusters, Vector centroid, Vector current) {
        double min = Double.MAX_VALUE;
        Vector centroidOfClosestCluster = null;
        for (Vector otherCentroid : clusters.keySet()) {
            if (!centroid.equals(otherCentroid)) {
                Set<Vector> vectors = clusters.get(otherCentroid);
                for (Vector vector : vectors) {
                    double distance = calculateManhattanDistance(current, vector);
                    if (distance < min) {
                        min = distance;
                        centroidOfClosestCluster = otherCentroid;
                    }
                }
            }
        }
        double distance = 0.0;
        Set<Vector> closestCluster = clusters.get(centroidOfClosestCluster);
        for (Vector v : closestCluster) {
            distance += calculateManhattanDistance(current, v);
        }
        return distance / closestCluster.size();
    }
}
