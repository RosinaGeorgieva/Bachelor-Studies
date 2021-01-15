package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.server.command.Command;
import bg.sofia.uni.fmi.mjt.wish.list.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.User;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.Wish;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.Repository;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.SessionsRepository;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.WishesRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandExecutorTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SUCCESSFULLY_REGISTERED_MSG = "[ Username rosi successfully registered ]" + LINE_SEPARATOR;

    private static final String REGISTER_ROSI = "1 register Rosi 12";
    private static final String REGISTER_VALID_USER_2 = "3 register Gabi jhg3";
    private static final String POST_WISH_WITH_VALID_USER_1 = "3 post-wish rosi kniga";
    private static final String POST_WISH_WITH_INVALID_USER = "3 post-wish niki topka";

    private static final String WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT = "[ The same gift for student Gabi was already submitted ]" + LINE_SEPARATOR;
    private static final String WISH_SUCCESSFULLY_SUBMITTED_MESSAGE = "[ Gift kniga for student rosi submitted successfully ]" + LINE_SEPARATOR;
    private static final String LOGOUT = "3 logout" + LINE_SEPARATOR;
    private static final String SUCCESSFUL_LOGOUT_MSG = "[ Successfully logged out ]" + LINE_SEPARATOR;

    private static final String NOT_LOGGED_IN_MSG = "[ You are not logged in ]" + LINE_SEPARATOR;
    private static final String NO_SUCH_REGISTERED_MSG = "[ Student with username niki is not registered ]" + LINE_SEPARATOR;
    private static final String NO_STUDENTS_IN_LIST_MESSAGE_FORMAT = "[ There are no students present in the wish list ]" + LINE_SEPARATOR;

    private static final String REGISTER_VALID_USER_3 = "3 register dANI jhg3";
    private static final String POST_WISH_WITH_ANOTHER_VALID_USER = "3 post-wish Gabi kote";

    private static final String REGISTER_VALID_USER_4 = "3 register Ivan 12";
    private static final String REGISTER_VALID_USER_5 = "3 register Desi 32";
    private static final String LOGIN_USER_4 = "3 login Ivan 12";
    private static final String POST_WISH_FOR_USER_4 = "3 post-wish Ivan kuche";
    private static final String POST_ANOTHER_WISH_FOR_USER_4 = "3 post-wish Ivan papagal";
    private static final String WISH_LIST_OF_USER_4 = "[ Ivan: [kuche, papagal] ]" + LINE_SEPARATOR;
    private static final String VALID_GET_WISH = "3 get-wish";

    private static final String OK = "-*&OK&*-" + LINE_SEPARATOR;

    private static final User ROSI = new User("Rosi");
    private static final User MIMI = new User("Mimi.B");
    private static final Wish DIPLOMA = new Wish("Diploma ot FMI");
    private static final Wish CATAN = new Wish("the Catan board game");
    private static final ArrayList<Wish> WISHES_OF_ROSI = new ArrayList<>(List.of(DIPLOMA));
    private static final ArrayList<Wish> WISHES_OF_MIMI = new ArrayList<>(List.of(DIPLOMA, CATAN));
    private static final String WISHLIST_OF_MIMI = "[ Mimi: [Diploma ot FMI, the Catan board game] ]";

    private static final Map<User, ArrayList<Wish>> wishesByUser = Map.of(ROSI, WISHES_OF_ROSI, MIMI, WISHES_OF_MIMI);
    private static final Map<Integer, User> sessionIdByUser = Map.of(1, ROSI, 2, MIMI);

    private static WishesRepository wishesRepository;

    private static SessionsRepository sessionsRepository;

    private static CommandExecutor commandExecutor;

    @BeforeClass
    public static void setUp() {
        wishesRepository = mock(WishesRepository.class);
        sessionsRepository = mock(SessionsRepository.class);
        commandExecutor = new CommandExecutor(wishesRepository, sessionsRepository) ;
    }

    @Test //ne bachka; :((
    public void testProcessRequestPostWishValid() {
//        when(wishesRepository.getAllEntries()).thenReturn(new HashMap<>());
        when(wishesRepository.add(ROSI, DIPLOMA)).thenReturn(WISH_SUCCESSFULLY_SUBMITTED_MESSAGE);

        Command command = new Command(1, "post-wish", new String[]{ROSI.toString(), DIPLOMA.toString()});
        assertEquals(WISH_SUCCESSFULLY_SUBMITTED_MESSAGE, commandExecutor.executeAuthenticated(OK, command));
    }

    @Test
    public void testProcessRequestPostAlreadyExistingWish() {
//        when(wishesRepository.add(ROSI, DIPLOMA)).thenReturn(WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT);

//        Command command = new Command(1, "post-wish", new String[]{ROSI.toString(), DIPLOMA.toString()});
//        assertEquals(WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT, commandExecutor.executeAuthenticated(OK, command));
    }

//    @Test //ne bachka
//    public void testProcessRequestGetWish() {
//        when(wishesRepository.getAllEntries()).thenReturn(wishesByUser);
//
//        Command command = new Command(1, "get-wish", new String[]{});
//        assertEquals(WISHLIST_OF_MIMI, commandExecutor.executeAuthenticated(OK, command));
//    }

//    @Test
//    public void testExecuteNoUsersAvailable() {
//        when(wishesRepository.getAllEntries()).thenReturn(new HashMap<>());
//
//
//    }
}
