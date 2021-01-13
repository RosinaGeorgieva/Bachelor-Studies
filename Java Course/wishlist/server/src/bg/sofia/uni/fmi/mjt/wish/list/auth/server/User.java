package bg.sofia.uni.fmi.mjt.wish.list.auth.server;

import java.io.Serializable;

public record User(String username, String password) implements Serializable {
}
