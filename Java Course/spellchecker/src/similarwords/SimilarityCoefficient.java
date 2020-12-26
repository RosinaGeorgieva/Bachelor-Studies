package similarwords;

public record SimilarityCoefficient(Double similarity) implements Comparable<SimilarityCoefficient> {
    @Override
    public int compareTo(SimilarityCoefficient other) {
        return similarity.compareTo(other.similarity());
    }
}
