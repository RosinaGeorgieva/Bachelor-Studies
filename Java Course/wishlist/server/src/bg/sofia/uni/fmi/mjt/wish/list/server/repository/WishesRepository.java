package bg.sofia.uni.fmi.mjt.wish.list.server.repository;

import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.User;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.Wish;
import bg.sofia.uni.fmi.mjt.wish.list.server.exception.NoUsersAvailableException;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WishesRepository implements Repository<User, Wish> {
    private static final String WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT = "[ The same gift for student %s was already submitted ]%s";
    private static final String WISH_SUCCESSFULLY_SUBMITTED_MESSAGE_FORMAT = "[ Gift %s for student %s submitted successfully ]%s";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final Map<User, ArrayList<Wish>> wishesByUser;

    public WishesRepository() {
        wishesByUser = new HashMap<>();
    }

    @Override
    public String add(User user, Wish wish) {
        if (wishesByUser.containsKey(user)) {
            List<Wish> wishesForThisStudent = wishesByUser.get(user);
            if (wishesForThisStudent.contains(wish)) {
                return String.format(WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT, user, LINE_SEPARATOR);
            }
            wishesForThisStudent.add(wish);
            return String.format(WISH_SUCCESSFULLY_SUBMITTED_MESSAGE_FORMAT, wish, user, LINE_SEPARATOR);
        }

        wishesByUser.put(user, new ArrayList<>(List.of(wish)));
        return String.format(WISH_SUCCESSFULLY_SUBMITTED_MESSAGE_FORMAT, wish, user, LINE_SEPARATOR);
    }

    @Override
    public void remove(User user) {
        wishesByUser.remove(user);
    }

    public Collection<Wish> get(User user) {
        return this.wishesByUser.get(user);
    }

    public Map<User, ArrayList<Wish>> getAllEntries() {
        return wishesByUser;
    }
}
