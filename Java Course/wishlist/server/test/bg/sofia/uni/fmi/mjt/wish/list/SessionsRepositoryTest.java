package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.User;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.SessionsRepository;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class SessionsRepositoryTest {
    private static final User ROSI = new User("Rosi");

    private static final SessionsRepository sessionsRepository = new SessionsRepository();

    @Test
    public void testAdd() {
        assertEquals(ROSI.toString(), sessionsRepository.add(2, ROSI));
        sessionsRepository.remove(2);
    }

    @Test
    public void testRemove() {
        sessionsRepository.add(1, new User(""));
        sessionsRepository.remove(1);
        assertEquals(new HashMap<>(), sessionsRepository.getAllEntries());
    }
}
