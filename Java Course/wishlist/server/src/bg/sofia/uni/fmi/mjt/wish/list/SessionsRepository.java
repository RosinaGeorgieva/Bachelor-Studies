package bg.sofia.uni.fmi.mjt.wish.list;

import java.util.HashMap;
import java.util.Map;

public class SessionsRepository {
    private static final Map<Integer, User> userBySession = new HashMap<>();

    public static void add(Integer sessionId, User user) {
        userBySession.put(sessionId, user); // da dobavq i proverkite za contains i tn
    }

    public static void remove(Integer sessionId) {
        userBySession.remove(sessionId); // proverkite
    }

    public static User getUserOf(Integer sessionId) {
        return userBySession.get(sessionId);
    }
}
