package bg.sofia.uni.fmi.mjt.wish.list.server.pojo;

import java.io.Serializable;
//da mahna implements serializable ot vsqkyde
public record User(String username) {
    @Override
    public String toString() {
        return this.username;
    }
}
