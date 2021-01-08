package bg.sofia.uni.fmi.mjt.wish.list;

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
    private static final Map<String, Set<Wish>> WISHES_BY_STUDENT = new HashMap<>();

    public String postWish(String student, Wish wish) { //omesena e logikata mnogo
        if (WISHES_BY_STUDENT.containsKey(student)) {
            Set<Wish> wishesForThisStudent = WISHES_BY_STUDENT.get(student);
            if (wishesForThisStudent.contains(wish)) {
                return String.format(WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT, student, LINE_SEPARATOR);
            } else {
                wishesForThisStudent.add(wish);
                return String.format(WISH_SUCCESSFULLY_SUBMITTED_MESSAGE_FORMAT, wish, student, LINE_SEPARATOR);
            }
        } else {
            WISHES_BY_STUDENT.put(student, new HashSet<>(Set.of(wish)));
            return String.format(WISH_SUCCESSFULLY_SUBMITTED_MESSAGE_FORMAT, wish, student, LINE_SEPARATOR);
        }
    }

    public String getRandomWishList() {
        if (WISHES_BY_STUDENT.size() == 0) {
            return String.format(NO_STUDENTS_IN_LIST_MESSAGE_FORMAT, LINE_SEPARATOR);
        }

        String chosenStudent = randomStudent();
        String wishList = getWishListFor(chosenStudent);

        WISHES_BY_STUDENT.remove(chosenStudent);
        return wishList;
    }

    private String randomStudent() {
        int randomNumber = ThreadLocalRandom.current().nextInt(0, WISHES_BY_STUDENT.size());
        List<String> students = new ArrayList<>(WISHES_BY_STUDENT.keySet());
        return students.get(randomNumber);
    }

    private String getWishListFor(String student) {
        Set<Wish> wishes = WISHES_BY_STUDENT.get(student);
        String message = String.format(WISH_LIST_FORMAT, student, wishes.toString(), LINE_SEPARATOR);
        return message;
    }
}
