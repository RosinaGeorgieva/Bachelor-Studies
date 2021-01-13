package bg.sofia.uni.fmi.mjt.wish.list.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class WishListRepository {
    private static final String WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT = "[ The same gift for student %s was already submitted ]%s";
    private static final String WISH_SUCCESSFULLY_SUBMITTED_MESSAGE_FORMAT = "[ Gift %s for student %s submitted successfully ]%s";
    private static final String NO_STUDENTS_IN_LIST_MESSAGE_FORMAT = "[ There are no students present in the wish list ]%s";
    private static final String WISH_LIST_FORMAT = "[ %s: %s ]%s";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final Map<User, ArrayList<Wish>> wishesByUser = new HashMap<>(); //by user da gi napravq vmesto by string

    public String postWish(User user, Wish wish) { //omesena e logikata mnogo
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

    public String getRandomWishList(Integer sessionId) {
        if (wishesByUser.size() == 0) {
            return String.format(NO_STUDENTS_IN_LIST_MESSAGE_FORMAT, LINE_SEPARATOR);
        }
        try {
            User chosenStudent = randomGiftRecipientFor(SessionsRepository.getUserOf(sessionId));
            String wishList = getWishListFor(chosenStudent);

            wishesByUser.remove(chosenStudent);
            return wishList;
        } catch (NoUsersAvailableException exception) {
            return String.format(NO_STUDENTS_IN_LIST_MESSAGE_FORMAT, LINE_SEPARATOR);
        }
    }

    private User randomGiftRecipientFor(User user) throws NoUsersAvailableException {
        List<User> users = new ArrayList<>(wishesByUser.keySet());
        if (users.size() == 1 && users.contains(user)) {
            throw new NoUsersAvailableException(NO_STUDENTS_IN_LIST_MESSAGE_FORMAT);
        }

        int randomNumber = ThreadLocalRandom.current().nextInt(0, wishesByUser.size());
        User randomUser = users.get(randomNumber);

        return randomUser;
    }

    private String getWishListFor(User user) {
        List<Wish> wishes = wishesByUser.get(user);
        String message = String.format(WISH_LIST_FORMAT, user, wishes.toString(), LINE_SEPARATOR);
        return message;
    }
}
