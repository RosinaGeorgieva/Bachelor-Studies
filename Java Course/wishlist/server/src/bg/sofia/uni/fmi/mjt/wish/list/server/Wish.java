package bg.sofia.uni.fmi.mjt.wish.list.server;

public record Wish(String wish) {
    @Override
    public String toString() {
        return this.wish;
    }
}
