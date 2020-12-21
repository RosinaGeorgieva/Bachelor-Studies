package similarwords;

public record SimilarityCoefficient(Double simmilarity) implements  Comparable<SimilarityCoefficient>{
    @Override
    public int compareTo(SimilarityCoefficient other) {
        if(simmilarity > other.simmilarity()){
            return 1;
        } else if (simmilarity < other.simmilarity){
            return -1;
        }
        return 0;
    }
}
