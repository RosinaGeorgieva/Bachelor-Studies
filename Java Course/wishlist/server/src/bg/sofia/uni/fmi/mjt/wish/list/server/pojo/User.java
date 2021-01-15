package bg.sofia.uni.fmi.mjt.wish.list.server.pojo;

public record User(String username) {
    @Override
    public String toString() {
        return this.username;
    }
}
