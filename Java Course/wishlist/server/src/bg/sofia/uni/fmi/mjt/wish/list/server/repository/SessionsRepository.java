package bg.sofia.uni.fmi.mjt.wish.list.server.repository;

import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SessionsRepository implements Repository<Integer, User> {
    private final Map<Integer, User> userBySession;

    public SessionsRepository() {
        userBySession = new HashMap<>();
    }

    @Override
    public String add(Integer sessionId, User user) {
        userBySession.put(sessionId, user); // da dobavq i proverkite za contains i tn
        return user.toString();
    }

    @Override
    public void remove(Integer sessionId) {
        userBySession.remove(sessionId); // proverkite
    }

    public User get(Integer sessionId) {
        return this.userBySession.get(sessionId);
    }

    public Map<Integer, User> getAllEntries() {
        return this.userBySession;
    }
}
