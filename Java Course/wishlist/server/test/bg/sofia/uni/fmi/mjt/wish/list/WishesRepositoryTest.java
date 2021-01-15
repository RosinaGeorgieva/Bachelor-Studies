package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.server.command.Command;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.User;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.Wish;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.Repository;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.WishesRepository;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WishesRepositoryTest {
    private static final User ROSI = new User("Rosi");
    private static final User MIMI = new User("Mimi.B");
    private static final Wish DIPLOMA = new Wish("Diploma ot FMI");
    private static final Wish CATAN = new Wish("the Catan board game");
    private static final ArrayList<Wish> WISHES_OF_MIMI = new ArrayList<>(List.of(DIPLOMA, CATAN));
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT = "[ The same gift for student Rosi was already submitted ]" + LINE_SEPARATOR;
    private static final String WISH_SUCCESSFULLY_SUBMITTED_MESSAGE = "[ Gift Diploma ot FMI for student Rosi submitted successfully ]" + LINE_SEPARATOR;
    private static final String SECOND_WISH_SUCCESSFULLY_SUBMITTED_MESSAGE = "[ Gift the Catan board game for student Rosi submitted successfully ]" + LINE_SEPARATOR;

    private static final Repository wishesRepository = new WishesRepository();

    @After
    public void cleanUp() {
        for(User user : ((WishesRepository)wishesRepository).getAllEntries().keySet()) {
            wishesRepository.remove(user);
        }
    }

    @Test
    public void testAdd() {
        assertEquals(WISH_SUCCESSFULLY_SUBMITTED_MESSAGE, wishesRepository.add(ROSI, DIPLOMA));
        assertEquals(WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT, wishesRepository.add(ROSI, DIPLOMA));
        assertEquals(SECOND_WISH_SUCCESSFULLY_SUBMITTED_MESSAGE, wishesRepository.add(ROSI, CATAN));
    }

    @Test
    public void testGet() {
        wishesRepository.add(MIMI, DIPLOMA);
        wishesRepository.add(MIMI, CATAN);
        assertEquals(List.of(DIPLOMA, CATAN), ((WishesRepository)wishesRepository).get(MIMI));
    }

    @Test
    public void testRemove() {
        wishesRepository.add(ROSI, DIPLOMA);
        wishesRepository.remove(ROSI);
        assertEquals(new HashMap<>(), ((WishesRepository)wishesRepository).getAllEntries());
    }
}
